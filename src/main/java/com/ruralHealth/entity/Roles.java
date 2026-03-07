package com.ruralHealth.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleId")
    private Long roleId;

    @ToString.Exclude // To avoid circular reference when printing the object
    @Enumerated(EnumType.STRING) // Store the enum as a string in the database
    @Column(name = "roleName")
    private Role roleName;

    public Roles(Role roleName) {
        this.roleName = roleName;
    }


}
