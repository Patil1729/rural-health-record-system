package com.ruralHealth.security.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {


//     ObjectMapper is a utility class from the Jackson library that is used for converting Java objects to JSON and vice versa.
  //  private final ObjectMapper objectMapper;

    // Logger for logging authentication errors or unauthorized access attempts.
    public static final Logger log = LoggerFactory.getLogger(JWTAuthenticationEntryPoint.class);

//    public JWTAuthenticationEntryPoint(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//    }

    @Override
    @SuppressWarnings("squid:S1160")
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // This method is called when an unauthenticated user tries to access a protected resource.
        // It logs the unauthorized access attempt and sends an HTTP 401 Unauthorized response back to the client.
        log.debug("Unauthorized error : {}", authException.getMessage());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("path", request.getServletPath());

        final ObjectMapper objectMapper = new ObjectMapper();
         objectMapper.writeValue(response.getOutputStream(), body);

    }



}
