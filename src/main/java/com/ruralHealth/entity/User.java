package com.ruralHealth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames ="userName"),
        @UniqueConstraint(columnNames = "email" )
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @NotBlank
    @Size(max = 20)
    @Column(name = "userName")
    private String userName;

    @NotBlank
    //@Size(max = 20)
    @Column(name = "password")
    private String password;

    @NotBlank
    @Size(max = 50)
    @Email
    @Column(name = "email")
    private String email;

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    @Getter
    @Setter
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable( //
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private Set<Roles> roles;


//
//    @Getter
//    @Setter
//    @OneToMany(mappedBy = "user",cascade = { CascadeType.PERSIST,CascadeType.MERGE}
//            ,orphanRemoval = true, fetch = FetchType.EAGER)
////    @JoinTable( name = "user_address",
////            joinColumns=@JoinColumn(name = "userId"),
////            inverseJoinColumns = @JoinColumn(name = "addressId")
////    )
//    private List< Address> address = new ArrayList<>();
//

}