package com.pasarku.ui;

import com.pasarku.controller.UserController;
import com.pasarku.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private JTable userTable;
    private UserController controller;
    private JButton addButton, editButton, deleteButton, resetPasswordButton;
    
    public UserManagementPanel() {
        this.controller = new UserController();
        initializeUI();
        loadUserData();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Table
        userTable = new JTable();
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(userTable), BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addButton = new JButton("Tambah");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Hapus");
        resetPasswordButton = new JButton("Reset Password");
        
        addButton.addActionListener(this::addUser);
        editButton.addActionListener(this::editUser);
        deleteButton.addActionListener(this::deleteUser);
        resetPasswordButton.addActionListener(this::resetPassword);
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(resetPasswordButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadUserData() {
        List<User> users = controller.getAllUsers();
        String[] columns = {"ID", "Username", "Email", "Role", "Tanggal Daftar"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        for (User u : users) {
            model.addRow(new Object[]{
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getRole(),
                u.getCreatedAt().toString()
            });
        }
        
        userTable.setModel(model);
    }
    
    private void addUser(ActionEvent e) {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField emailField = new JTextField();
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"user", "admin"});
        
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Role:"));
        panel.add(roleCombo);
        
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Tambah User Baru", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            controller.addUser(
                usernameField.getText(),
                new String(passwordField.getPassword()),
                emailField.getText(),
                (String) roleCombo.getSelectedItem()
            );
            loadUserData();
        }
    }
    
    private void editUser(ActionEvent e) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih user terlebih dahulu");
            return;
        }
        
        int id = (int) userTable.getValueAt(selectedRow, 0);
        User user = controller.getUserById(id);
        
        if (user == null) return;
        
        JTextField usernameField = new JTextField(user.getUsername());
        JTextField emailField = new JTextField(user.getEmail());
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"user", "admin"});
        roleCombo.setSelectedItem(user.getRole());
        
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Role:"));
        panel.add(roleCombo);
        
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Edit User", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            controller.updateUser(
                user,
                usernameField.getText(),
                emailField.getText(),
                (String) roleCombo.getSelectedItem()
            );
            loadUserData();
        }
    }
    
    private void deleteUser(ActionEvent e) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih user terlebih dahulu");
            return;
        }
        
        int id = (int) userTable.getValueAt(selectedRow, 0);
        User user = controller.getUserById(id);
        
        if (user == null) return;
        
        // Prevent self-deletion
        if (user.getId() == SessionManager.getCurrentUser().getId()) {
            JOptionPane.showMessageDialog(this, "Tidak bisa menghapus akun sendiri");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Hapus user " + user.getUsername() + "?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteUser(user);
            loadUserData();
        }
    }
    
    private void resetPassword(ActionEvent e) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih user terlebih dahulu");
            return;
        }
        
        int id = (int) userTable.getValueAt(selectedRow, 0);
        User user = controller.getUserById(id);
        
        if (user == null) return;
        
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmField = new JPasswordField();
        
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Password Baru:"));
        panel.add(passwordField);
        panel.add(new JLabel("Konfirmasi Password:"));
        panel.add(confirmField);
        
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Reset Password untuk " + user.getUsername(), 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());
            String confirm = new String(confirmField.getPassword());
            
            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Password tidak cocok");
                return;
            }
            
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password minimal 6 karakter");
                return;
            }
            
            controller.resetPassword(user, password);
            JOptionPane.showMessageDialog(this, "Password berhasil direset");
        }
    }
}