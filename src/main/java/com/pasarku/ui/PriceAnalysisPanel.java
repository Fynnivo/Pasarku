package com.pasarku.ui;

import com.pasarku.controller.PriceController;
import com.pasarku.model.Harga;
import org.jfree.chart.ChartFactory;  // Perhatikan titik setelah "org"
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PriceAnalysisPanel extends JPanel {
    private PriceController controller;
    private JComboBox<String> analysisTypeCombo;
    private JButton generateButton;
    
    public PriceAnalysisPanel() {
        this.controller = new PriceController();
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Controls panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        analysisTypeCombo = new JComboBox<>(new String[]{
            "Harga Rata-Rata per Produk", 
            "Harga per Pasar", 
            "Perkembangan Harga"
        });
        generateButton = new JButton("Generate");
        generateButton.addActionListener(e -> generateChart());
        
        controlPanel.add(new JLabel("Analisis:"));
        controlPanel.add(analysisTypeCombo);
        controlPanel.add(generateButton);
        add(controlPanel, BorderLayout.NORTH);
    }
    
    private void generateChart() {
        String selectedAnalysis = (String) analysisTypeCombo.getSelectedItem();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        switch (selectedAnalysis) {
            case "Harga Rata-Rata per Produk":
                // Implement logic to calculate average prices per product
                break;
            case "Harga per Pasar":
                // Implement logic to show prices per market
                break;
            case "Perkembangan Harga":
                // Implement logic to show price trends
                break;
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
            selectedAnalysis,
            "Kategori",
            "Harga (Rp)",
            dataset
        );
        
        // Remove previous chart if exists
        Component[] components = getComponents();
        if (components.length > 1) {
            remove(components[1]);
        }
        
        add(new ChartPanel(chart), BorderLayout.CENTER);
        revalidate();
    }
}