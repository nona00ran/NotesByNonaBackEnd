package com.jovana.notesbynona.service.impl;

import com.jovana.notesbynona.entity.Role;
import com.jovana.notesbynona.entity.User;
import com.jovana.notesbynona.exceptions.DataNotFoundError;
import com.jovana.notesbynona.exceptions.EmailAlreadyExistsException;
import com.jovana.notesbynona.exceptions.UsernameAlreadyExistsException;
import com.jovana.notesbynona.model.enums.RoleName;
import com.jovana.notesbynona.model.user.UserUpdateRequest;
import com.jovana.notesbynona.repository.RoleRepository;
import com.jovana.notesbynona.repository.UserRepository;
import com.jovana.notesbynona.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
   public User createUser(User newUser) {
       Role userRole = RoleServiceImpl.getRoleByName(RoleName.USER);
       if (userRole == null) {
           throw new IllegalStateException("Role USER not found in cache");
       }
       newUser.setUsername(newUser.getUsername().toLowerCase());
       userRepository.findByUsername(newUser.getUsername()).ifPresent(user -> {
           throw new UsernameAlreadyExistsException("Username already in use.");
       });
       userRepository.findByEmail(newUser.getEmail()).ifPresent(user -> {
           throw new EmailAlreadyExistsException("Email is already in use.");
       });
       newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
       newUser.setRoles(new HashSet<>(Collections.singletonList(userRole)));
       return userRepository.save(newUser);
   }

    public Set<Role> getUserRoles(String username) {
        Optional<User> user = findByUsername(username);
        if (user.isPresent()) {
            return user.get().getRoles();
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public List<String> getUserAuthorities(String username) {
        Optional<User> user = findByUsername(username);
        if (user.isPresent()) {
            Set<Role> roles = user.get().getRoles();
            List<String> authorities = new ArrayList<>();
            for (Role role : roles) {
                authorities.add(role.getName().name());
            }
            return authorities;
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    private Set<Role> mapRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return Set.of();
        }
        return roleNames.stream()
                .map(roleName -> getRoleByName(RoleName.valueOf(roleName.toUpperCase())))
                .collect(Collectors.toSet());
    }
    public Role getRoleByName(RoleName name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            throw new IllegalArgumentException("Role not found: " + name);
        }
        return role;
    }
    @Override
    public User updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundError("User not found with id: " + userId));
        user.setRoles(mapRoles(userUpdateRequest.getRoles()));
        user.setFirstName(userUpdateRequest.getFirstName());
        user.setLastName(userUpdateRequest.getLastName());
        user.setEmail(userUpdateRequest.getEmail());
        user.setUsername(userUpdateRequest.getUsername());
        if (userUpdateRequest.getPassword() != null && !userUpdateRequest.getPassword().isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(userUpdateRequest.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    public void updateUserAuthorities(Long userId, boolean addAdmin) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundError("User not found with id: " + userId));

        Role adminRole = getRoleByName(RoleName.ADMIN);

        if (addAdmin) {
            user.getRoles().add(adminRole);
        } else {
            user.getRoles().remove(adminRole);
        }

        userRepository.save(user);
    }

    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}