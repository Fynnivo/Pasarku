package com.pasarku.ui;

import com.pasarku.controller.ProductController;
import com.pasarku.model.Produk;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ProdukManagementPanel extends JPanel {
    private JTable produkTable;
    private ProductController controller;
    private JButton addButton, editButton, deleteButton;
    
    public ProdukManagementPanel() {
        this.controller = new ProductController();
        initializeUI();
        loadProductData();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Table
        produkTable = new JTable();
        produkTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(produkTable), BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addButton = new JButton("Tambah");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Hapus");
        
        addButton.addActionListener(this::addProduct);
        editButton.addActionListener(this::editProduct);
        deleteButton.addActionListener(this::deleteProduct);
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadProductData() {
        List<Produk> products = controller.getAllProducts();
        String[] columns = {"ID", "Nama", "Kategori", "Satuan", "Kualitas"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        for (Produk p : products) {
            model.addRow(new Object[]{
                p.getId(),
                p.getNama(),
                p.getKategori(),
                p.getSatuan(),
                p.getKualitas()
            });
        }
        
        produkTable.setModel(model);
    }
    
    private void addProduct(ActionEvent e) {
        JTextField namaField = new JTextField();
        JComboBox<String> kategoriCombo = new JComboBox<>(new String[]{
            "beras", "sayuran", "buah", "protein", "bumbu", "minyak_gula", "lainnya"
        });
        JTextField satuanField = new JTextField();
        JComboBox<String> kualitasCombo = new JComboBox<>(new String[]{"A", "B", "C"});
        
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Nama:"));
        panel.add(namaField);
        panel.add(new JLabel("Kategori:"));
        panel.add(kategoriCombo);
        panel.add(new JLabel("Satuan:"));
        panel.add(satuanField);
        panel.add(new JLabel("Kualitas:"));
        panel.add(kualitasCombo);
        
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Tambah Produk Baru", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            controller.addProduct(
                namaField.getText(),
                (String) kategoriCombo.getSelectedItem(),
                satuanField.getText(),
                (String) kualitasCombo.getSelectedItem()
            );
            loadProductData();
        }
    }
    
    private void editProduct(ActionEvent e) {
        int selectedRow = produkTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih produk terlebih dahulu");
            return;
        }
        
        int id = (int) produkTable.getValueAt(selectedRow, 0);
        Produk produk = controller.getAllProducts().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
        
        if (produk == null) return;
        
        JTextField namaField = new JTextField(produk.getNama());
        JComboBox<String> kategoriCombo = new JComboBox<>(new String[]{
            "beras", "sayuran", "buah", "protein", "bumbu", "minyak_gula", "lainnya"
        });
        kategoriCombo.setSelectedItem(produk.getKategori());
        JTextField satuanField = new JTextField(produk.getSatuan());
        JComboBox<String> kualitasCombo = new JComboBox<>(new String[]{"A", "B", "C"});
        kualitasCombo.setSelectedItem(produk.getKualitas());
        
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Nama:"));
        panel.add(namaField);
        panel.add(new JLabel("Kategori:"));
        panel.add(kategoriCombo);
        panel.add(new JLabel("Satuan:"));
        panel.add(satuanField);
        panel.add(new JLabel("Kualitas:"));
        panel.add(kualitasCombo);
        
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Edit Produk", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            controller.updateProduct(
                produk,
                namaField.getText(),
                (String) kategoriCombo.getSelectedItem(),
                satuanField.getText(),
                (String) kualitasCombo.getSelectedItem()
            );
            loadProductData();
        }
    }
    
    private void deleteProduct(ActionEvent e) {
        int selectedRow = produkTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih produk terlebih dahulu");
            return;
        }
        
        int id = (int) produkTable.getValueAt(selectedRow, 0);
        Produk produk = controller.getAllProducts().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
        
        if (produk == null) return;
        
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Hapus produk " + produk.getNama() + "?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteProduct(produk);
            loadProductData();
        }
    }
}