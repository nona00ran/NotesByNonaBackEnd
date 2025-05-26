package com.jovana.notesbynona.service;

import com.jovana.notesbynona.entity.Role;
import com.jovana.notesbynona.entity.User;
import com.jovana.notesbynona.model.user.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    User createUser(User user);

    User getUser(Long id);

    Page<User> getAllUsers(Pageable pageable);

    Optional<User> findByUsername(String username);

    Set<Role> getUserRoles(String username);

    List<String> getUserAuthorities(String username);

    User updateUser(Long userId, UserUpdateRequest userUpdateRequest);

    void updateUserAuthorities(Long userId, boolean addAdmin);

    boolean verifyPassword(String rawPassword, String encodedPassword);
}