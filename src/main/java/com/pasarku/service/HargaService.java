package com.pasarku.service;

import com.pasarku.dao.GenericDao;
import com.pasarku.model.Harga;
import com.pasarku.model.Pasar;
import com.pasarku.model.Produk;
import java.util.List;
import java.util.stream.Collectors;

public class HargaService {
    private GenericDao<Harga, Integer> hargaDao = new GenericDao<>(Harga.class);
    
    public void save(Harga harga) {
        hargaDao.save(harga);
    }
    
    public Harga findById(int id) {
        return hargaDao.findById(id);
    }
    
    public List<Harga> findAll() {
        return hargaDao.findAll();
    }
    
    public void delete(Harga harga) {
        hargaDao.delete(harga);
    }
    
    public List<Harga> findByProduk(Produk produk) {
        return findAll().stream()
                .filter(h -> h.getProduk().getId() == produk.getId())
                .collect(Collectors.toList());
    }
    
    public List<Harga> findByPasar(Pasar pasar) {
        return findAll().stream()
                .filter(h -> h.getPasar().getId() == pasar.getId())
                .collect(Collectors.toList());
    }
}