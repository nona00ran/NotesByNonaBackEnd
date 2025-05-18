package com.jovana.notesbynona.repository;

import com.jovana.notesbynona.entity.Role;
import com.jovana.notesbynona.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName name);
}





