package com.ruralHealth.controller;

import com.ruralHealth.config.JWTUtils;
import com.ruralHealth.entity.Role;
import com.ruralHealth.entity.Roles;
import com.ruralHealth.entity.User;
import com.ruralHealth.repository.RolesRepository;
import com.ruralHealth.repository.UserRepository;
import com.ruralHealth.request.LoginRequest;
import com.ruralHealth.request.SignUpRequest;
import com.ruralHealth.response.MessageResponse;
import com.ruralHealth.response.UserInfoResponse;
import com.ruralHealth.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    // This controller will handle authentication requests and generate JWT tokens for authenticated users.

   private static final Logger log = LoggerFactory.getLogger(AuthRestController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    // The JWTUtils class is likely a utility class that provides methods for generating and validating JWT tokens,
    // which will be used in the authentication process.
    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolesRepository roleRepository;

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello World";
    }


    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // This method will handle user registration requests. It will likely accept user details (e.g., username, password, email) and
        // create a new user account in the system. After successful registration, it may return a response indicating that the user has been registered successfully.
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            Map<String,Object> body = new HashMap<>();
            body.put("error", "Unauthorized");
            body.put("message", e.getMessage());
            body.put("status", 401);
            body.put("path", "/api/sign-in");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
        }
        //to store authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        /// String jwtToken = jwtUtils.generateJWTTokenFromUserName(userDetails);

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        log.debug("JWT cookie : {}", jwtCookie);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(item -> item.getAuthority()).collect(Collectors.toList());

        ///UserInfoResponse response = new UserInfoResponse(userDetails.getId(),jwtToken,roles,userDetails.getUsername());
        UserInfoResponse response = new  UserInfoResponse(userDetails.getId(),jwtCookie.toString(),roles,userDetails.getUsername());

        // to pass cookie to frontend we need to set cookie in header and send response body as well
        return  ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(response);

       /// return  ResponseEntity.ok(response);


    }

   // @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest request){

        if(userRepository.existsByUserName(request.getUsername())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : UserName is already Taken"));
        }

        if(userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : Email is already Taken"));
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())

        );

        Set<String> strRole = request.getRole();
        Set<Roles> roles = new HashSet<>();

        if(strRole == null){
            Roles userRole = roleRepository.findByRoleName(Role.USER)
                    .orElseThrow(()-> new RuntimeException("Error : Role not found"));
            roles.add(userRole);
        }else {
            strRole.forEach(role -> {
                switch (role) {
                    case "admin":
                        Roles adminRole = roleRepository.findByRoleName(Role.ADMIN)
                                .orElseThrow(()-> new RuntimeException("Error : Role not found"));
                        roles.add(adminRole);
                        break;

                    case "doctor":
                        Roles doctorRole = roleRepository.findByRoleName(Role.DOCTOR)
                                .orElseThrow(()-> new RuntimeException("Error : Role not found"));
                        roles.add(doctorRole);
                        break;

                    case "patient":
                        Roles patientRole = roleRepository.findByRoleName(Role.PATIENT)
                                .orElseThrow(()-> new RuntimeException("Error : Role not found"));
                        roles.add(patientRole);
                        break;

                   case "nurse":
                       Roles nurseRole = roleRepository.findByRoleName(Role.NURSE)
                               .orElseGet(() -> {
                                   Roles newRole = new Roles(Role.NURSE);
                                   return roleRepository.save(newRole);
                               });
                       roles.add(nurseRole);
                       break;

                    default:
                        Roles userRole = roleRepository.findByRoleName(Role.USER)
                                .orElseThrow(()-> new RuntimeException("Error : Role not found"));
                        roles.add(userRole);

                }
            });
        }


        user.setRoles(roles);
        userRepository.save(user);
        log.debug("User {} registered successfully with roles: {}", user.getUserName(),
                roles.stream().map(Roles::getRoleName));
        return ResponseEntity.ok(new MessageResponse("Users registered successfully"));

    }

    @GetMapping("/username")
    public String currentUsername( Authentication authentication){
        if(authentication !=null){
            return authentication.getName();
        }else {
            return "";
        }
    }


    @GetMapping("/user")
    public ResponseEntity<?> currentUserDetails(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(item -> item.getAuthority()).collect(Collectors.toList());

        UserInfoResponse response = new  UserInfoResponse(userDetails.getId(),roles,userDetails.getUsername());

        return   ResponseEntity.ok()
                .body(response);

    }


    @PostMapping("/signout")
    public ResponseEntity<?> signOutUser(){
        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();

        return  ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new MessageResponse("You've been logged out successfully"));
    }





}
