package com.pasarku.ui;

import com.pasarku.controller.UserController;
import com.pasarku.model.User;
import com.pasarku.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private final UserController userController;
    
    public UserManagementPanel() {
        this.userController = new UserController();
        initializeUI();
        loadUserData();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Table setup
        String[] columns = {"ID", "Username", "Email", "Role", "Tanggal Daftar"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setAutoCreateRowSorter(true);
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        addButton(buttonPanel, "Tambah", this::handleAddUser);
        addButton(buttonPanel, "Edit", this::handleEditUser);
        addButton(buttonPanel, "Hapus", this::handleDeleteUser);
        addButton(buttonPanel, "Reset Password", this::handleResetPassword);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void addButton(JPanel panel, String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 30));
        button.addActionListener(listener);
        panel.add(button);
    }
    
    private void loadUserData() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                List<User> users = userController.getAllUsers();
                SwingUtilities.invokeLater(() -> {
                    tableModel.setRowCount(0); // Clear existing data
                    for (User user : users) {
                        tableModel.addRow(new Object[]{
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getRole(),
                            user.getCreatedAt().toString()
                        });
                    }
                });
                return null;
            }
            
            @Override
            protected void done() {
                // Optional: Add loading indicator cleanup
            }
        };
        worker.execute();
    }
    
    private void handleAddUser(ActionEvent e) {
        UserFormDialog dialog = new UserFormDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            "Tambah User Baru",
            null,
            (username, password, email, role) -> {
                try {
                    userController.addUser(username, password, email, role);
                    loadUserData();
                    showSuccess("User berhasil ditambahkan");
                    return true;
                } catch (IllegalArgumentException ex) {
                    showError(ex.getMessage());
                    return false;
                } catch (Exception ex) {
                    showError("Terjadi kesalahan: " + ex.getMessage());
                    return false;
                }
            }
        );
        dialog.setVisible(true);
    }
    
    private void handleEditUser(ActionEvent e) {
        User selectedUser = getSelectedUser();
        if (selectedUser == null) return;
        
        if (selectedUser.getId() == SessionManager.getCurrentUser().getId()) {
            showError("Tidak bisa mengedit akun sendiri");
            return;
        }
        
        UserFormDialog dialog = new UserFormDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            "Edit User",
            selectedUser,
            (username, password, email, role) -> {
                try {
                    userController.updateUser(selectedUser, username, email, role);
                    loadUserData();
                    showSuccess("User berhasil diupdate");
                    return true;
                } catch (Exception ex) {
                    showError(ex.getMessage());
                    return false;
                }
            }
        );
        dialog.setVisible(true);
    }
    
    private void handleDeleteUser(ActionEvent e) {
        User selectedUser = getSelectedUser();
        if (selectedUser == null) return;
        
        if (selectedUser.getId() == SessionManager.getCurrentUser().getId()) {
            showError("Tidak bisa menghapus akun sendiri");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus user " + selectedUser.getUsername() + "?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                userController.deleteUser(selectedUser);
                loadUserData();
                showSuccess("User berhasil dihapus");
            } catch (Exception ex) {
                showError("Gagal menghapus user: " + ex.getMessage());
            }
        }
    }
    
    private void handleResetPassword(ActionEvent e) {
        User selectedUser = getSelectedUser();
        if (selectedUser == null) return;
        
        PasswordResetDialog dialog = new PasswordResetDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            selectedUser.getUsername(),
            (newPassword) -> {
                try {
                    userController.resetPassword(selectedUser, newPassword);
                    showSuccess("Password berhasil direset");
                    return true;
                } catch (Exception ex) {
                    showError("Gagal reset password: " + ex.getMessage());
                    return false;
                }
            }
        );
        dialog.setVisible(true);
    }
    
    private User getSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Pilih user terlebih dahulu");
            return null;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        return userController.getUserById(id);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Nested dialog classes for better organization
    private static class UserFormDialog extends JDialog {
        public UserFormDialog(JFrame parent, String title, User user, UserFormListener listener) {
            super(parent, title, true);
            setSize(400, 300);
            setLocationRelativeTo(parent);
            
            JTextField usernameField = new JTextField(20);
            JPasswordField passwordField = new JPasswordField(20);
            JTextField emailField = new JTextField(20);
            JComboBox<String> roleCombo = new JComboBox<>(new String[]{"user", "admin"});
            
            if (user != null) {
                usernameField.setText(user.getUsername());
                emailField.setText(user.getEmail());
                roleCombo.setSelectedItem(user.getRole());
                passwordField.setEnabled(false); // Disable password field for edit
            }
            
            JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            panel.add(new JLabel("Username:"));
            panel.add(usernameField);
            panel.add(new JLabel("Password:"));
            panel.add(passwordField);
            panel.add(new JLabel("Email:"));
            panel.add(emailField);
            panel.add(new JLabel("Role:"));
            panel.add(roleCombo);
            
            JButton saveButton = new JButton("Simpan");
            saveButton.addActionListener(e -> {
                if (listener.onSubmit(
                    usernameField.getText(),
                    new String(passwordField.getPassword()),
                    emailField.getText(),
                    (String) roleCombo.getSelectedItem()
                )) {
                    dispose();
                }
            });
            
            JButton cancelButton = new JButton("Batal");
            cancelButton.addActionListener(e -> dispose());
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.add(cancelButton);
            buttonPanel.add(saveButton);
            
            add(panel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }
        
        @FunctionalInterface
        interface UserFormListener {
            boolean onSubmit(String username, String password, String email, String role);
        }
    }
    
    private static class PasswordResetDialog extends JDialog {
        public PasswordResetDialog(JFrame parent, String username, PasswordResetListener listener) {
            super(parent, "Reset Password untuk " + username, true);
            setSize(350, 200);
            setLocationRelativeTo(parent);
            
            JPasswordField passwordField = new JPasswordField(20);
            JPasswordField confirmField = new JPasswordField(20);
            
            JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            panel.add(new JLabel("Password Baru:"));
            panel.add(passwordField);
            panel.add(new JLabel("Konfirmasi Password:"));
            panel.add(confirmField);
            
            JButton saveButton = new JButton("Simpan");
            saveButton.addActionListener(e -> {
                String password = new String(passwordField.getPassword());
                String confirm = new String(confirmField.getPassword());
                
                if (!password.equals(confirm)) {
                    JOptionPane.showMessageDialog(this, "Password tidak cocok", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (password.length() < 6) {
                    JOptionPane.showMessageDialog(this, "Password minimal 6 karakter", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (listener.onSubmit(password)) {
                    dispose();
                }
            });
            
            JButton cancelButton = new JButton("Batal");
            cancelButton.addActionListener(e -> dispose());
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.add(cancelButton);
            buttonPanel.add(saveButton);
            
            add(panel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }
        
        @FunctionalInterface
        interface PasswordResetListener {
            boolean onSubmit(String newPassword);
        }
    }
}