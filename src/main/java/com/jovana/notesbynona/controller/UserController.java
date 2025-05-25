package com.jovana.notesbynona.controller;

import com.jovana.notesbynona.entity.Role;
import com.jovana.notesbynona.entity.User;
import com.jovana.notesbynona.model.login.LoginResponse;
import com.jovana.notesbynona.model.user.UserUpdateRequest;
import com.jovana.notesbynona.service.JwtService;
import com.jovana.notesbynona.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/users")
@Validated
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<LoginResponse> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        String token = jwtService.createToken(createdUser);
        LoginResponse loginResponse = new LoginResponse(token, jwtService.createRefreshToken(createdUser,  null), "Created account successfully");
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username.toLowerCase());
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(Pageable pageable) {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getRoles/{username}")
    public ResponseEntity<Set<Role>> getUserRoles(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserRoles(username.toLowerCase()));
    }

    @GetMapping("/getAuthorities/{username}")
    public ResponseEntity<List<String>> getUserAuthorities(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserAuthorities(username.toLowerCase()));
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId,
                                           @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.updateUser(userId, userUpdateRequest);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/updateAuthorities/{userId}")
    public ResponseEntity<Void> updateUserAuthorities(@PathVariable Long userId,
                                                      @RequestParam boolean addAdmin) {
        userService.updateUserAuthorities(userId, addAdmin);
        return ResponseEntity.noContent().build();
    }
}
