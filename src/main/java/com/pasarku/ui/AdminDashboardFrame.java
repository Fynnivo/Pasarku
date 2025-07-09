package com.pasarku.ui;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class AdminDashboardFrame extends DashboardFrame {
    public AdminDashboardFrame() {
        super("Admin Dashboard");
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Harga", new HargaTablePanel());
        tabbedPane.addTab("Input Harga", new InputHargaPanel());
        tabbedPane.addTab("Kelola Produk", new ProdukManagementPanel());
        tabbedPane.addTab("Kelola Pasar", new PasarManagementPanel());
        tabbedPane.addTab("Kelola User", new UserManagementPanel());
        tabbedPane.addTab("Analisis Harga", new PriceAnalysisPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Add user info panel
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.add(new JLabel("Admin: " + SessionManager.getCurrentUser().getUsername()));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> SessionManager.logout());
        userPanel.add(logoutButton);
        add(userPanel, BorderLayout.NORTH);
    }
}