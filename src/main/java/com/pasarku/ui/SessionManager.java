package com.pasarku.ui;

import com.pasarku.model.User;

import javax.swing.*;

public class SessionManager {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
        JOptionPane.showMessageDialog(null, "Logout sukses!");
        new LoginFrame().setVisible(true);
    }
}
