package com.pasarku.service;

import com.pasarku.dao.GenericDao;
import com.pasarku.model.Produk;
import java.util.List;

public class ProdukService {
    private GenericDao<Produk, Integer> produkDao = new GenericDao<>(Produk.class);

    public void save(Produk produk) {
        produkDao.save(produk);
    }

    public Produk findById(int id) {
        return produkDao.findById(id);
    }

    public List<Produk> findAll() {
        return produkDao.findAll();
    }

    public void delete(Produk produk) {
        produkDao.delete(produk);
    }
}
