package com.pasarku.controller;

import com.pasarku.model.User;
import com.pasarku.model.User.UserRole;
import com.pasarku.service.UserService;
import com.pasarku.util.PasswordUtil;
import java.time.LocalDateTime;
import java.util.List;

public class UserController {
    private final UserService userService;
    
    public UserController() {
        this.userService = new UserService();
    }
    
    public User authenticate(String username, String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password tidak boleh kosong");
        }

        User user = userService.findByUsername(username);
        if (user != null && PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }
    
    public void registerUser(User user) throws IllegalArgumentException {
        validateRegistration(user.getUsername(), user.getPasswordHash());

        if (userService.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username sudah terdaftar");
        }

        userService.register(user);
    }
    
    public void addUser(String username, String password, String email, String roleString) 
        throws IllegalArgumentException {

        // Validasi input
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username harus diisi");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password harus diisi");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email harus diisi");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password minimal 6 karakter");
        }

        // Konversi role string ke enum
        UserRole role = convertStringToUserRole(roleString);

        // Cek username unik
        if (userService.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username sudah terdaftar");
        }

        // Buat user baru
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(PasswordUtil.hashPassword(password));
        user.setEmail(email);
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());

        userService.register(user);
    }
    
    private void validateRegistration(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username harus diisi");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password harus diisi");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password minimal 6 karakter");
        }
        if (userService.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username sudah terdaftar");
        }
    }
    
    public UserRole determineUserRole() {
        return userService.findAll().isEmpty() ? UserRole.ADMIN : UserRole.USER;
    }
    
    public List<User> getAllUsers() {
        return userService.findAll();
    }
    
    public User getUserById(int id) {
        return userService.findById(id);
    }
    
    public void updateUser(User user, String username, String email, String roleString) {
        UserRole role = convertStringToUserRole(roleString);
        updateUser(user, username, email, role);
    }
    
    // Versi yang menerima enum UserRole langsung (untuk penggunaan internal)
    private void updateUser(User user, String username, String email, UserRole role) {
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(role);
        userService.updateUser(user);
    }
    
    // Helper method untuk konversi String ke UserRole
    private UserRole convertStringToUserRole(String roleString) {
        try {
            // Coba konversi dari format enum (ADMIN/USER)
            return UserRole.valueOf(roleString.toUpperCase());
        } catch (IllegalArgumentException e1) {
            try {
                // Coba konversi dari format database (admin/user)
                return UserRole.fromDbValue(roleString);
            } catch (IllegalArgumentException e2) {
                throw new IllegalArgumentException(
                    "Role tidak valid: " + roleString + 
                    ". Gunakan 'admin'/'user' atau 'ADMIN'/'USER'"
                );
            }
        }
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