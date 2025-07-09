package com.pasarku.service;

import com.pasarku.dao.GenericDao;
import com.pasarku.model.Pasar;
import java.util.List;

public class PasarService {
    private GenericDao<Pasar, Integer> pasarDao = new GenericDao<>(Pasar.class);

    public void save(Pasar pasar) {
        pasarDao.save(pasar);
    }

    public Pasar findById(int id) {
        return pasarDao.findById(id);
    }

    public List<Pasar> findAll() {
        return pasarDao.findAll();
    }

    public void delete(Pasar pasar) {
        pasarDao.delete(pasar);
    }
}
