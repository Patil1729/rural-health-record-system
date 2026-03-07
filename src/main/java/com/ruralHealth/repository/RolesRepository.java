package com.ruralHealth.repository;

import com.ruralHealth.entity.Role;
import com.ruralHealth.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository  extends JpaRepository<Roles,Long> {

    Optional<Roles> findByRoleName(Role roleName);

}
