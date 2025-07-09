package com.pasarku.ui;

import com.pasarku.controller.MarketController;
import com.pasarku.model.Pasar;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PasarManagementPanel extends JPanel {
    private JTable pasarTable;
    private MarketController controller;
    private JButton addButton, editButton, deleteButton;
    
    public PasarManagementPanel() {
        this.controller = new MarketController();
        initializeUI();
        loadMarketData();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Table
        pasarTable = new JTable();
        pasarTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(pasarTable), BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addButton = new JButton("Tambah");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Hapus");
        
        addButton.addActionListener(this::addMarket);
        editButton.addActionListener(this::editMarket);
        deleteButton.addActionListener(this::deleteMarket);
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadMarketData() {
        List<Pasar> markets = controller.getAllMarkets();
        String[] columns = {"ID", "Nama", "Alamat", "Kota", "Jam Operasional", "Hari Pasar", "Kategori"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        for (Pasar p : markets) {
            model.addRow(new Object[]{
                p.getId(),
                p.getNama(),
                p.getAlamat(),
                p.getKota(),
                p.getJamOperasional(),
                p.getHariPasar(),
                p.getKategori()
            });
        }
        
        pasarTable.setModel(model);
    }
    
    private void addMarket(ActionEvent e) {
        JTextField namaField = new JTextField();
        JTextField alamatField = new JTextField();
        JTextField kotaField = new JTextField();
        JTextField jamField = new JTextField("08:00-17:00");
        JTextField hariField = new JTextField();
        JComboBox<String> kategoriCombo = new JComboBox<>(new String[]{"harian", "mingguan", "khusus"});
        
        JPanel panel = new JPanel(new GridLayout(7, 2));
        panel.add(new JLabel("Nama Pasar:"));
        panel.add(namaField);
        panel.add(new JLabel("Alamat:"));
        panel.add(alamatField);
        panel.add(new JLabel("Kota:"));
        panel.add(kotaField);
        panel.add(new JLabel("Jam Operasional:"));
        panel.add(jamField);
        panel.add(new JLabel("Hari Pasar:"));
        panel.add(hariField);
        panel.add(new JLabel("Kategori:"));
        panel.add(kategoriCombo);
        
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Tambah Pasar Baru", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            controller.addMarket(
                namaField.getText(),
                alamatField.getText(),
                kotaField.getText(),
                jamField.getText(),
                hariField.getText(),
                (String) kategoriCombo.getSelectedItem()
            );
            loadMarketData();
        }
    }
    
    private void editMarket(ActionEvent e) {
        int selectedRow = pasarTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih pasar terlebih dahulu");
            return;
        }
        
        int id = (int) pasarTable.getValueAt(selectedRow, 0);
        Pasar pasar = controller.getAllMarkets().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
        
        if (pasar == null) return;
        
        JTextField namaField = new JTextField(pasar.getNama());
        JTextField alamatField = new JTextField(pasar.getAlamat());
        JTextField kotaField = new JTextField(pasar.getKota());
        JTextField jamField = new JTextField(pasar.getJamOperasional());
        JTextField hariField = new JTextField(pasar.getHariPasar());
        JComboBox<String> kategoriCombo = new JComboBox<>(new String[]{"harian", "mingguan", "khusus"});
        kategoriCombo.setSelectedItem(pasar.getKategori());
        
        JPanel panel = new JPanel(new GridLayout(7, 2));
        panel.add(new JLabel("Nama Pasar:"));
        panel.add(namaField);
        panel.add(new JLabel("Alamat:"));
        panel.add(alamatField);
        panel.add(new JLabel("Kota:"));
        panel.add(kotaField);
        panel.add(new JLabel("Jam Operasional:"));
        panel.add(jamField);
        panel.add(new JLabel("Hari Pasar:"));
        panel.add(hariField);
        panel.add(new JLabel("Kategori:"));
        panel.add(kategoriCombo);
        
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Edit Pasar", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            controller.updateMarket(
                pasar,
                namaField.getText(),
                alamatField.getText(),
                kotaField.getText(),
                jamField.getText(),
                hariField.getText(),
                (String) kategoriCombo.getSelectedItem()
            );
            loadMarketData();
        }
    }
    
    private void deleteMarket(ActionEvent e) {
        int selectedRow = pasarTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih pasar terlebih dahulu");
            return;
        }
        
        int id = (int) pasarTable.getValueAt(selectedRow, 0);
        Pasar pasar = controller.getAllMarkets().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
        
        if (pasar == null) return;
        
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Hapus pasar " + pasar.getNama() + "?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteMarket(pasar);
            loadMarketData();
        }
    }
}