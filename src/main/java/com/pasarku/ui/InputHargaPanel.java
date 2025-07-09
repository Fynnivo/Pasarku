package com.pasarku.ui;

import com.pasarku.model.Harga;
import com.pasarku.model.Pasar;
import com.pasarku.model.Produk;
import com.pasarku.model.User;
import com.pasarku.service.HargaService;
import com.pasarku.service.PasarService;
import com.pasarku.service.ProdukService;
import com.pasarku.ui.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.List;

public class InputHargaPanel extends JPanel {
    private JComboBox<Produk> produkCombo;
    private JComboBox<Pasar> pasarCombo;
    private JTextField hargaField;
    private JButton submitButton;
    
    private ProdukService produkService = new ProdukService();
    private PasarService pasarService = new PasarService();
    private HargaService hargaService = new HargaService();
    
    public InputHargaPanel() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleLabel = new JLabel("Input Harga Baru");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, gbc);
        
        // Produk selection
        gbc.gridwidth = 1;
        gbc.gridy++;
        add(new JLabel("Produk:"), gbc);
        
        gbc.gridx = 1;
        produkCombo = new JComboBox<>();
        loadProdukData();
        add(produkCombo, gbc);
        
        // Pasar selection
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Pasar:"), gbc);
        
        gbc.gridx = 1;
        pasarCombo = new JComboBox<>();
        loadPasarData();
        add(pasarCombo, gbc);
        
        // Harga input
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Harga:"), gbc);
        
        gbc.gridx = 1;
        hargaField = new JTextField(10);
        add(hargaField, gbc);
        
        // Submit button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        submitButton = new JButton("Simpan");
        submitButton.addActionListener(this::submitAction);
        add(submitButton, gbc);
    }
    
    private void loadProdukData() {
        try {
            List<Produk> produkList = produkService.findAll();
            produkCombo.removeAllItems();
            for (Produk p : produkList) {
                produkCombo.addItem(p);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading produk: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadPasarData() {
        try {
            List<Pasar> pasarList = pasarService.findAll();
            pasarCombo.removeAllItems();
            for (Pasar p : pasarList) {
                pasarCombo.addItem(p);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading pasar: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void submitAction(ActionEvent e) {
        try {
            Produk selectedProduk = (Produk) produkCombo.getSelectedItem();
            Pasar selectedPasar = (Pasar) pasarCombo.getSelectedItem();
            double harga = Double.parseDouble(hargaField.getText());
            
            if (selectedProduk == null || selectedPasar == null) {
                JOptionPane.showMessageDialog(this, "Pilih produk dan pasar terlebih dahulu", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (harga <= 0) {
                JOptionPane.showMessageDialog(this, "Harga harus lebih dari 0", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Harga newHarga = new Harga();
            newHarga.setProduk(selectedProduk);
            newHarga.setPasar(selectedPasar);
            newHarga.setHarga(harga);
            newHarga.setWaktuUpdate(LocalDateTime.now());
            newHarga.setUser(SessionManager.getCurrentUser());
            
            hargaService.save(newHarga);
            
            JOptionPane.showMessageDialog(this, "Harga berhasil disimpan!");
            hargaField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan harga yang valid", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}