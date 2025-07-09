package com.pasarku.controller;

import com.pasarku.model.Harga;
import com.pasarku.model.Pasar;
import com.pasarku.model.Produk;
import com.pasarku.model.User;
import com.pasarku.service.HargaService;
import java.time.LocalDateTime;
import java.util.List;

public class PriceController {
    private final HargaService hargaService;
    
    public PriceController() {
        this.hargaService = new HargaService();
    }
    
    public List<Harga> getAllPrices() {
        return hargaService.findAll();
    }
    
    public void addPrice(Produk produk, Pasar pasar, double harga, User user) {
        Harga newHarga = new Harga();
        newHarga.setProduk(produk);
        newHarga.setPasar(pasar);
        newHarga.setHarga(harga);
        newHarga.setWaktuUpdate(LocalDateTime.now());
        newHarga.setUser(user);
        hargaService.save(newHarga);
    }
    
    public void updatePrice(Harga harga, double newHarga) {
        harga.setHarga(newHarga);
        harga.setWaktuUpdate(LocalDateTime.now());
        hargaService.save(harga);
    }
    
    public void deletePrice(Harga harga) {
        hargaService.delete(harga);
    }
    
    public List<Harga> getPricesByProduk(Produk produk) {
        return hargaService.findByProduk(produk);
    }
    
    public List<Harga> getPricesByPasar(Pasar pasar) {
        return hargaService.findByPasar(pasar);
    }
    
    // Add this method to PriceController.java
    public Harga getLatestPriceForProduct(Produk produk) {
        return hargaService.findAll().stream()
                .filter(h -> h.getProduk().getId() == produk.getId())
                .max((h1, h2) -> h1.getWaktuUpdate().compareTo(h2.getWaktuUpdate()))
                .orElse(null);
}
}