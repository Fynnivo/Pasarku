package com.pasarku.controller;

import com.pasarku.model.User;
import com.pasarku.service.UserService;
import com.pasarku.util.PasswordUtil;
import java.time.LocalDateTime;
import java.util.List;

public class UserController {
    private final UserService userService;
    
    public UserController() {
        this.userService = new UserService();
    }
    
    public List<User> getAllUsers() {
        return userService.findAll();
    }
    
    public User getUserById(int id) {
        return userService.findById(id);
    }
    
    public void addUser(String username, String password, String email, String role) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password required");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(PasswordUtil.hashPassword(password));
        user.setEmail(email);
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        userService.register(user);
    }
    
    public void updateUser(User user, String username, String email, String role) {
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(role);
        userService.updateUser(user);
    }
    
    public void deleteUser(User user) {
        userService.deleteUser(user);
    }
    
    public void resetPassword(User user, String newPassword) {
        user.setPasswordHash(PasswordUtil.hashPassword(newPassword));
        userService.updateUser(user);
    }
    
    public List<User> searchUsers(String keyword) {
        return userService.findAll().stream()
                .filter(u -> u.getUsername().toLowerCase().contains(keyword.toLowerCase()) ||
                             u.getEmail().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }
}