package com.pasarku.ui;

import com.pasarku.controller.UserController;
import java.time.LocalDateTime;
import com.pasarku.model.User;
import com.pasarku.util.PasswordUtil;
import com.pasarku.util.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private final UserController userController;
    
    public LoginFrame() {
        this.userController = new UserController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("PasarKu - Login/Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleLabel = new JLabel("PasarKu - Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, gbc);
        
        // Username
        gbc.gridwidth = 1;
        gbc.gridy++;
        mainPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        mainPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        mainPanel.add(passwordField, gbc);
        
        // Buttons
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30));
        JButton registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(100, 30));
        
        loginButton.addActionListener(this::handleLogin);
        registerButton.addActionListener(this::handleRegister);
        passwordField.addActionListener(this::handleLogin);
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Username dan password harus diisi");
            return;
        }
        
        try {
            User user = userController.authenticate(username, password);
            if (user != null) {
                SessionManager.setCurrentUser(user);
                showSuccess("Login sukses!");
                openDashboard(user);
                dispose();
            } else {
                showError("Username/password salah");
            }
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void handleRegister(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username dan password harus diisi");
            return;
        }

        if (password.length() < 6) {
            showError("Password minimal 6 karakter");
            return;
        }

        try {
            // Create a new user with all required fields
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPasswordHash(PasswordUtil.hashPassword(password));
            newUser.setEmail(username + "@pasarku.com");
            newUser.setRole(userController.determineUserRole()); // Use controller's existing method
            newUser.setCreatedAt(LocalDateTime.now());

            userController.registerUser(newUser);
            showSuccess("Registrasi sukses! Silakan login.");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void openDashboard(User user) {
        if (user.getRole() == User.UserRole.ADMIN) {
            new AdminDashboardFrame().setVisible(true);
        } else {
            new UserDashboardFrame().setVisible(true);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }
}