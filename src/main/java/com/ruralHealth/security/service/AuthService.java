package com.ruralHealth.security.service;


import com.ruralHealth.security.request.AuthenticationResult;
import com.ruralHealth.security.request.LoginRequest;
import com.ruralHealth.security.request.SignUpRequest;
import com.ruralHealth.security.response.MessageResponse;
import com.ruralHealth.security.response.UserInfoResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {

    AuthenticationResult login(LoginRequest loginRequest);

    ResponseEntity<MessageResponse> register(SignUpRequest signUpRequest);

    UserInfoResponse getCurrentUserDetails(Authentication authentication);

    ResponseCookie logoutUser();


}
