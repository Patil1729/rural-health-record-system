package com.ruralHealth.jwtUtility;


import com.ruralHealth.service.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JWTUtils {

private static final Logger log = LoggerFactory.getLogger(JWTUtils.class);

    @Value("${spring.app.jwtExpirationInMs}")
    private String jwtExpirationInMs; // "3600000"; Example expiration time (1 hour)

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret; //"your_jwt_secret_key"; Example secret key for signing the JWT


    public String getJWTFromHeader(HttpServletRequest request){
        // This method is a placeholder for extracting the JWT token from the HTTP request header.
        // In a real implementation, you would typically look for the "Authorization" header and extract the token from it.

        String bearerToken = request.getHeader("Authorization");
        log.debug("Authorization Header : {}", bearerToken);
        if(bearerToken !=null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7); // Remove "Bearer " prefix to get the actual token
        }
        return null; // Return null if the token is not present or does not start with "
    }

    public String generateJWTTokenFromUserName(UserDetailsImpl userDetails){
        // This method is a placeholder for generating a JWT token based on the provided username.
        // In a real implementation, you would use a library like jjwt to create the token, set its claims, and sign it with the secret key.

        String userName = userDetails.getUsername();
        // Here, we are using the Jwts builder to create a JWT token. We set the subject to the username,
        // the issued date to the current date, and the expiration date to the current date plus the specified expiration time.
        // Finally, we sign the token with the secret key and compact it into a string format.
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + Long.parseLong(jwtExpirationInMs)))
                .signWith(key())
                .compact();

    }

    public String generateUserNameFromJWTToken(String token){
        // This method is a placeholder for extracting the username from the provided JWT token.
        // In a real implementation, you would use a library like jjwt to parse the token,
        // verify its signature, and extract the claims.

        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseClaimsJws(token)
                .getPayload()
                .getSubject();
    }

    public Key key(){
        // This method is a placeholder for generating a signing key based on the secret key.
        // In a real implementation, you would typically use a library like jjwt to create a SecretKey from the provided secret string.

        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    public boolean validateJWTToken(String authToken){
        // This method is a placeholder for validating the provided JWT token.
        // In a real implementation, you would use a library like jjwt to parse the token,
        // verify its signature, and check its expiration.

        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseClaimsJws(authToken);
            return true; // Token is valid
        }catch (MalformedJwtException exception){
            log.error("Invalid JWT token: {}", exception.getMessage());
        }
        catch (Exception e) {
            log.error("JWT token expired: {}", e.getMessage());

        }
        return false; // Token is invalid
    }

}
