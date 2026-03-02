package com.ruralHealth.jwtUtility;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthJWTTokenFilter extends OncePerRequestFilter {
    // This filter will intercept incoming HTTP requests and check for the presence of a JWT token in the Authorization header.
    // If a valid token is found, it will set the authentication context for the request,
    // allowing the user to access protected resources. If the token is invalid or missing,
    // the filter will not set the authentication context, and the request will be handled by
    // the authentication entry point, which will return an appropriate response (e.g., 401 Unauthorized) to the client.

    @Autowired
    private  JWTUtils jwtUtils;
    @Autowired
    private UserDetailsService userDetailsService;

    private static final Logger log = LoggerFactory.getLogger(AuthJWTTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.debug("AuthJWTTokenFilter called for : {}", request.getRequestURI());

        try {
            String jwt = parseToken(request);
            if (jwt != null && jwtUtils.validateJWTToken(jwt)) {
                String userName = jwtUtils.generateUserNameFromJWTToken(jwt);
                var userDetails = userDetailsService.loadUserByUsername(userName);
                // UsernamePasswordAuthenticationToken is a Spring Security class that represents an authentication request or an authenticated user.
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());
// The setDetails method is used to set additional details about the authentication request, such as the remote IP address and session ID.
// This information can be useful for auditing and logging purposes.
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //set the authentication object in the SecurityContextHolder,
                // which is a central place for storing security-related information in Spring Security.
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Roles from JWT : {}", authentication.getAuthorities());

            }
        } catch (Exception e) {
            log.error("Cannot set authentication : {}", e.getMessage());
        }
// The filterChain.doFilter(request, response) method is called to pass the request and response to the next filter in the chain. This allows the request to continue through the filter chain and eventually reach the intended destination (e.g., a controller or resource). If the authentication was successful, the user will be able to access protected resources; otherwise,
// they will receive an appropriate response (e.g., 401 Unauthorized) from the authentication entry point.
           filterChain.doFilter(request,response);

    }


    private String parseToken(HttpServletRequest request) {
        String jwt = jwtUtils.getJWTFromHeader(request);
        if (jwt != null && jwtUtils.validateJWTToken(jwt)) {
            return jwt;
        }
        return null;
    }

}
