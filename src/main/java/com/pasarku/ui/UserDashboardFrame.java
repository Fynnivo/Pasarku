package com.pasarku.ui;

import javax.swing.*;
import java.awt.BorderLayout;

public class UserDashboardFrame extends DashboardFrame {
    public UserDashboardFrame() {
        super("User Dashboard");
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Tabel Harga", new HargaTablePanel());
        tabbedPane.addTab("Input Harga", new InputHargaPanel());
        tabbedPane.addTab("Daftar Belanja", new ShoppingListPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
}
