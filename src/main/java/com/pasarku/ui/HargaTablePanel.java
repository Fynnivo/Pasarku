package com.pasarku.ui;

import com.pasarku.model.Harga;
import com.pasarku.service.HargaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class HargaTablePanel extends JPanel {
    private JTable hargaTable;
    private HargaService hargaService = new HargaService();
    
    public HargaTablePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create table with model
        hargaTable = new JTable();
        hargaTable.setAutoCreateRowSorter(true);
        
        // Add refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(this::refreshAction);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        
        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(hargaTable), BorderLayout.CENTER);
        
        loadHargaData();
    }
    
    private void refreshAction(ActionEvent e) {
        loadHargaData();
    }
    
    public void loadHargaData() {
        try {
            List<Harga> hargaList = hargaService.findAll();
            String[] columns = {"ID", "Produk", "Pasar", "Harga", "Satuan", "Waktu Update", "Input Oleh"};
            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            for (Harga h : hargaList) {
                model.addRow(new Object[]{
                    h.getId(),
                    h.getProduk().getNama(),
                    h.getPasar().getNama(),
                    String.format("%,.2f", h.getHarga()),
                    h.getProduk().getSatuan(),
                    h.getWaktuUpdate().toString(),
                    h.getUser().getUsername()
                });
            }
            
            hargaTable.setModel(model);
            
            // Set column widths
            hargaTable.getColumnModel().getColumn(0).setPreferredWidth(50);
            hargaTable.getColumnModel().getColumn(1).setPreferredWidth(150);
            hargaTable.getColumnModel().getColumn(2).setPreferredWidth(150);
            hargaTable.getColumnModel().getColumn(3).setPreferredWidth(80);
            hargaTable.getColumnModel().getColumn(4).setPreferredWidth(60);
            hargaTable.getColumnModel().getColumn(5).setPreferredWidth(180);
            hargaTable.getColumnModel().getColumn(6).setPreferredWidth(100);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}