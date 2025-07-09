package com.pasarku.ui;

import com.pasarku.controller.PriceController;
import com.pasarku.controller.ProductController;
import com.pasarku.model.Harga;
import com.pasarku.model.Produk;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListPanel extends JPanel {
    private JTable shoppingTable;
    private JTable productTable;
    private JButton addButton, removeButton, calculateButton;
    private JLabel totalLabel;
    private List<Produk> selectedProducts = new ArrayList<>();
    private ProductController productController;
    private PriceController priceController;
    
    public ShoppingListPanel() {
        this.productController = new ProductController();
        this.priceController = new PriceController();
        initializeUI();
        loadProductData();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Split pane for product selection and shopping list
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        
        // Product selection panel
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBorder(BorderFactory.createTitledBorder("Daftar Produk"));
        productTable = new JTable();
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        
        // Shopping list panel
        JPanel shoppingPanel = new JPanel(new BorderLayout());
        shoppingPanel.setBorder(BorderFactory.createTitledBorder("Daftar Belanja"));
        shoppingTable = new JTable();
        shoppingPanel.add(new JScrollPane(shoppingTable), BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        addButton = new JButton("Tambah ke Belanja");
        removeButton = new JButton("Hapus dari Belanja");
        calculateButton = new JButton("Hitung Total");
        totalLabel = new JLabel("Total: Rp 0");
        
        addButton.addActionListener(this::addToShoppingList);
        removeButton.addActionListener(this::removeFromShoppingList);
        calculateButton.addActionListener(this::calculateTotal);
        
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(calculateButton);
        buttonPanel.add(totalLabel);
        
        shoppingPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        splitPane.setTopComponent(productPanel);
        splitPane.setBottomComponent(shoppingPanel);
        splitPane.setDividerLocation(0.5);
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void loadProductData() {
        List<Produk> products = productController.getAllProducts();
        String[] columns = {"ID", "Nama", "Kategori", "Satuan"};
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
                p.getSatuan()
            });
        }
        
        productTable.setModel(model);
    }
    
    private void addToShoppingList(ActionEvent e) {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih produk terlebih dahulu");
            return;
        }
        
        int id = (int) productTable.getValueAt(selectedRow, 0);
        Produk product = productController.getAllProducts().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
        
        if (product != null && !selectedProducts.contains(product)) {
            selectedProducts.add(product);
            updateShoppingList();
        }
    }
    
    private void removeFromShoppingList(ActionEvent e) {
        int selectedRow = shoppingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih item terlebih dahulu");
            return;
        }
        
        if (selectedRow < selectedProducts.size()) {
            selectedProducts.remove(selectedRow);
            updateShoppingList();
        }
    }
    
    private void updateShoppingList() {
        String[] columns = {"Produk", "Satuan", "Harga Terakhir"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        for (Produk p : selectedProducts) {
            Harga latestPrice = priceController.getLatestPriceForProduct(p);
            String price = latestPrice != null ? 
                String.format("Rp %,.2f", latestPrice.getHarga()) : "N/A";
            
            model.addRow(new Object[]{
                p.getNama(),
                p.getSatuan(),
                price
            });
        }
        
        shoppingTable.setModel(model);
    }
    
    private void calculateTotal(ActionEvent e) {
        if (selectedProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Daftar belanja kosong");
            return;
        }
        
        double total = 0;
        for (Produk p : selectedProducts) {
            Harga latestPrice = priceController.getLatestPriceForProduct(p);
            if (latestPrice != null) {
                total += latestPrice.getHarga();
            }
        }
        
        totalLabel.setText(String.format("Total: Rp %,.2f", total));
    }
}