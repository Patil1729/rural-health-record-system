package com.ruralHealth.security.service;

import com.ruralHealth.entity.Role;
import com.ruralHealth.entity.Roles;
import com.ruralHealth.entity.User;
import com.ruralHealth.repository.RolesRepository;
import com.ruralHealth.repository.UserRepository;
import com.ruralHealth.security.jwt.JwtUtils;
import com.ruralHealth.security.request.AuthenticationResult;
import com.ruralHealth.security.request.LoginRequest;
import com.ruralHealth.security.request.SignUpRequest;
import com.ruralHealth.security.response.MessageResponse;
import com.ruralHealth.security.response.UserInfoResponse;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RolesRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public AuthenticationResult login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        log.debug("JWT cookie : {}", jwtCookie);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

//        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),
//                userDetails.getUsername(), roles, userDetails.getEmail(), jwtCookie.toString());
        UserInfoResponse response = new  UserInfoResponse(userDetails.getId(),jwtCookie.toString(),roles,userDetails.getUsername());

        return new AuthenticationResult(response, jwtCookie);
    }

    @Override
    public ResponseEntity<MessageResponse> register(SignUpRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRole = signUpRequest.getRole();
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

    @Override
    public UserInfoResponse getCurrentUserDetails(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new  UserInfoResponse(userDetails.getId(),roles,userDetails.getUsername());

        return response;
    }

    @Override
    public ResponseCookie logoutUser() {
        return jwtUtils.getCleanJwtCookie();
    }




}
