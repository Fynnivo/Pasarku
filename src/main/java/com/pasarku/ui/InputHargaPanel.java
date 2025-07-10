package com.pasarku.ui;

import com.pasarku.controller.MarketController;
import com.pasarku.controller.ProductController;
import com.pasarku.model.Harga;
import com.pasarku.model.Pasar;
import com.pasarku.model.Produk;
import com.pasarku.model.User;
import com.pasarku.service.HargaService;
import com.pasarku.ui.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.List;

public class InputHargaPanel extends JPanel {
    private JComboBox<String> produkCombo;
    private JComboBox<String> pasarCombo;
    private JTextField hargaField;
    private JButton submitButton;
    
    private final ProductController productController;
    private final MarketController marketController;
    private final HargaService hargaService;
    
    private List<Produk> produkList;
    private List<Pasar> pasarList;
    
    public InputHargaPanel() {
        this.productController = new ProductController();
        this.marketController = new MarketController();
        this.hargaService = new HargaService();
        
        initializeUI();
        loadData();
    }
    
    private void initializeUI() {
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
        add(produkCombo, gbc);
        
        // Pasar selection
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Pasar:"), gbc);
        
        gbc.gridx = 1;
        pasarCombo = new JComboBox<>();
        add(pasarCombo, gbc);
        
        // Harga input
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Harga (Rp):"), gbc);
        
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
    
    private void loadData() {
        loadProdukData();
        loadPasarData();
    }
    
    private void loadProdukData() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {  // Explicitly specify types
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    produkList = productController.getAllProducts();
                    produkCombo.removeAllItems();
                    for (Produk p : produkList) {
                        produkCombo.addItem(p.getNama());
                    }
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> 
                        showError("Error loading produk: " + e.getMessage()));
                }
                return null;
            }

            @Override
            protected void done() {
                if (!produkList.isEmpty()) {
                    produkCombo.setSelectedIndex(0);
                }
            }
        };
        worker.execute();
    }

    private void loadPasarData() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {  // Explicitly specify types
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    pasarList = marketController.getAllMarkets();
                    pasarCombo.removeAllItems();
                    for (Pasar p : pasarList) {
                        pasarCombo.addItem(p.getNama());
                    }
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> 
                        showError("Error loading pasar: " + e.getMessage()));
                }
                return null;
            }

            @Override
            protected void done() {
                if (!pasarList.isEmpty()) {
                    pasarCombo.setSelectedIndex(0);
                }
            }
        };
        worker.execute();
    }
    
    private void submitAction(ActionEvent e) {
        int selectedProdukIndex = produkCombo.getSelectedIndex();
        int selectedPasarIndex = pasarCombo.getSelectedIndex();
        
        if (selectedProdukIndex < 0 || selectedPasarIndex < 0) {
            showError("Pilih produk dan pasar terlebih dahulu");
            return;
        }
        
        Produk selectedProduk = produkList.get(selectedProdukIndex);
        Pasar selectedPasar = pasarList.get(selectedPasarIndex);
        
        String hargaText = hargaField.getText().trim();
        if (hargaText.isEmpty()) {
            showError("Masukkan harga terlebih dahulu");
            return;
        }
        
        double harga;
        try {
            harga = Double.parseDouble(hargaText);
        } catch (NumberFormatException ex) {
            showError("Format harga tidak valid");
            return;
        }
        
        if (harga <= 0) {
            showError("Harga harus lebih dari 0");
            return;
        }
        
        saveHarga(selectedProduk, selectedPasar, harga);
    }
    
    private void saveHarga(Produk produk, Pasar pasar, double harga) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {  // Explicit types
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    Harga existingHarga = findExistingHarga(produk, pasar);
                    User currentUser = SessionManager.getCurrentUser();

                    if (existingHarga != null) {
                        existingHarga.setHarga(harga);
                        existingHarga.setWaktuUpdate(LocalDateTime.now());
                        existingHarga.setUser(currentUser);
                        hargaService.save(existingHarga);
                    } else {
                        Harga newHarga = new Harga();
                        newHarga.setProduk(produk);
                        newHarga.setPasar(pasar);
                        newHarga.setHarga(harga);
                        newHarga.setWaktuUpdate(LocalDateTime.now());
                        newHarga.setUser(currentUser);
                        hargaService.save(newHarga);
                    }

                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(InputHargaPanel.this, 
                            "Harga berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        hargaField.setText("");
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> 
                        showError("Gagal menyimpan harga: " + ex.getMessage()));
                }
                return null;
            }
        };
        worker.execute();
    }
    
    private Harga findExistingHarga(Produk produk, Pasar pasar) {
        List<Harga> allHarga = hargaService.findAll();
        return allHarga.stream()
                .filter(h -> h.getProduk().getId() == produk.getId() && 
                            h.getPasar().getId() == pasar.getId())
                .findFirst()
                .orElse(null);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}