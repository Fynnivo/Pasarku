package com.pasarku.controller;

import com.pasarku.model.Pasar;
import com.pasarku.service.PasarService;
import java.util.List;

public class MarketController {
    private final PasarService pasarService;
    
    public MarketController() {
        this.pasarService = new PasarService();
    }
    
    public List<Pasar> getAllMarkets() {
        return pasarService.findAll();
    }
    
    public void addMarket(String nama, String alamat, String kota, 
                         String jamOperasional, String hariPasar, String kategori) {
        Pasar pasar = new Pasar();
        pasar.setNama(nama);
        pasar.setAlamat(alamat);
        pasar.setKota(kota);
        pasar.setJamOperasional(jamOperasional);
        pasar.setHariPasar(hariPasar);
        pasar.setKategori(kategori);
        pasarService.save(pasar);
    }
    
    public void updateMarket(Pasar pasar, String nama, String alamat, String kota,
                           String jamOperasional, String hariPasar, String kategori) {
        pasar.setNama(nama);
        pasar.setAlamat(alamat);
        pasar.setKota(kota);
        pasar.setJamOperasional(jamOperasional);
        pasar.setHariPasar(hariPasar);
        pasar.setKategori(kategori);
        pasarService.save(pasar);
    }
    
    public void deleteMarket(Pasar pasar) {
        pasarService.delete(pasar);
    }
    
    public Pasar getMarketById(int id) {
        return pasarService.findById(id);
    }
    
    public List<Pasar> searchMarkets(String keyword) {
        return pasarService.findAll().stream()
                .filter(p -> p.getNama().toLowerCase().contains(keyword.toLowerCase()) ||
                             p.getKota().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }
}