package com.ruralHealth.security.controller;

import com.ruralHealth.repository.UserRepository;
import com.ruralHealth.security.request.AuthenticationResult;
import com.ruralHealth.security.request.LoginRequest;
import com.ruralHealth.security.request.SignUpRequest;
import com.ruralHealth.security.response.MessageResponse;
import com.ruralHealth.security.response.UserDTO;
import com.ruralHealth.security.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        AuthenticationResult result = authService.login(loginRequest);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        result.getJwtCookie().toString())
                .body(result.getResponse());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authService.register(signUpRequest);
    }

    @GetMapping("/username")
    public String currentUserName(Authentication authentication){
        if (authentication != null)
            return authentication.getName();
        else
            return "";
    }


    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication){
        return ResponseEntity.ok().body(authService.getCurrentUserDetails(authentication));
    }

    @GetMapping("/allusers")
    public ResponseEntity<?> getAllUsers() {

        List<UserDTO> users = userRepository.findAll()
                .stream()
                .map(user -> new UserDTO(
                        user.getUserId(),
                        user.getUserName(),
                        user.getEmail(),
                        user.getRoles().stream()
                                .map(role -> role.getRoleName().toString())
                                .collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signoutUser(){
        ResponseCookie cookie = authService.logoutUser();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }



}

