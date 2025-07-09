package com.pasarku.controller;

import com.pasarku.model.Produk;
import com.pasarku.service.ProdukService;
import java.util.List;

public class ProductController {
    private final ProdukService produkService;
    
    public ProductController() {
        this.produkService = new ProdukService();
    }
    
    public List<Produk> getAllProducts() {
        return produkService.findAll();
    }
    
    public void addProduct(String nama, String kategori, String satuan, String kualitas) {
        Produk produk = new Produk();
        produk.setNama(nama);
        produk.setKategori(kategori);
        produk.setSatuan(satuan);
        produk.setKualitas(kualitas);
        produkService.save(produk);
    }
    
    public void updateProduct(Produk produk, String nama, String kategori, String satuan, String kualitas) {
        produk.setNama(nama);
        produk.setKategori(kategori);
        produk.setSatuan(satuan);
        produk.setKualitas(kualitas);
        produkService.save(produk);
    }
    
    public void deleteProduct(Produk produk) {
        produkService.delete(produk);
    }
}