package com.ruralHealth.security.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class UserDTO {

    private Long userId;
    private String userName;
    //private String password;
    private String email;
    private Set<String> roles;

    public UserDTO(Long userId, String userName, String email, Set<String> roles) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.roles = roles;
    }

}
