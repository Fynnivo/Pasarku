package com.pasarku.service;

import com.pasarku.dao.GenericDao;
import com.pasarku.model.User;
import java.util.List;

public class UserService {
    private GenericDao<User, Integer> userDao = new GenericDao<>(User.class);

    public void register(User user) {
        userDao.save(user);
    }
    
    public void updateUser(User user) {
        userDao.save(user);
    }
    
    public void deleteUser(User user) {
        userDao.delete(user);
    }

    public User findById(int id) {
        return userDao.findById(id);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User findByUsername(String username) {
        return userDao.findAll().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }
}