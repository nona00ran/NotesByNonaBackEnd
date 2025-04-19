package com.jovana.notesbynona.service.impl;

import com.jovana.notesbynona.entity.Role;
import com.jovana.notesbynona.model.enums.RoleName;
import com.jovana.notesbynona.repository.RoleRepository;
import com.jovana.notesbynona.service.RoleService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;


@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private static final Map<RoleName, Role> rolesCache = new EnumMap<>(RoleName.class);

    @PostConstruct
    @Override
    public void initRoles() {
        for (RoleName roleName : RoleName.values()) {
            Role role_fetched = roleRepository.findByName(roleName);
            Role role;
            if (role_fetched == null) {
                role = new Role();
                role.setName(roleName);
                role = roleRepository.save(role);
            } else {
                role = role_fetched;
            }
            rolesCache.put(roleName, role);
        }
    }

    public static Role getRoleByName(RoleName roleName) {
        return rolesCache.get(roleName);
    }
}
