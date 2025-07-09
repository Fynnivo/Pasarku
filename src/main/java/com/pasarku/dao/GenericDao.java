package com.pasarku.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.pasarku.util.HibernateUtil;
import java.util.List;
import java.util.Optional;
import java.io.Serializable;

public class GenericDao<T, ID extends Serializable> {
    private final Class<T> entityClass;

    public GenericDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T save(T entity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(entity);
            tx.commit();
            System.out.println("Entity saved successfully: " + entity);
            return entity;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error saving entity: " + e.getMessage());
            throw new RuntimeException("Error saving entity of type " + entityClass.getSimpleName(), e);
        }
    }

    // Perbaikan: Kembalikan T langsung, bukan Optional<T>
    public T findById(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(entityClass, id);
        } catch (Exception e) {
            System.err.println("Error finding entity by id: " + id + " - " + e.getMessage());
            throw new RuntimeException("Error finding entity by id", e);
        }
    }

    // Tambahan: Method untuk mendapatkan Optional jika diperlukan
    public Optional<T> findByIdOptional(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            T entity = session.get(entityClass, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Error finding entity by id: " + id + " - " + e.getMessage());
            throw new RuntimeException("Error finding entity by id", e);
        }
    }

    public List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM " + entityClass.getSimpleName();
            Query<T> query = session.createQuery(hql, entityClass);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error retrieving all entities: " + e.getMessage());
            throw new RuntimeException("Error finding all entities of type " + entityClass.getSimpleName(), e);
        }
    }

    public void delete(T entity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            // Perbaikan: Pastikan entity terikat dengan session
            session.delete(session.contains(entity) ? entity : session.merge(entity));
            tx.commit();
            System.out.println("Entity deleted successfully: " + entity);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error deleting entity: " + e.getMessage());
            throw new RuntimeException("Error deleting entity of type " + entityClass.getSimpleName(), e);
        }
    }

    // Perbaikan: Tambahkan method deleteById untuk kemudahan
    public void deleteById(ID id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            T entity = session.get(entityClass, id);
            if (entity != null) {
                session.delete(entity);
            }
            tx.commit();
            System.out.println("Entity with id " + id + " deleted successfully");
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error deleting entity by id: " + id + " - " + e.getMessage());
            throw new RuntimeException("Error deleting entity by id", e);
        }
    }

    public List<T> findByAttribute(String attributeName, Object value) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM " + entityClass.getSimpleName() + " WHERE " + attributeName + " = :value";
            Query<T> query = session.createQuery(hql, entityClass);
            query.setParameter("value", value);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding by attribute: " + attributeName + " - " + e.getMessage());
            throw new RuntimeException("Error finding by attribute", e);
        }
    }

    // Perbaikan: Tambahkan method untuk mencari single entity berdasarkan atribut
    public T findSingleByAttribute(String attributeName, Object value) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM " + entityClass.getSimpleName() + " WHERE " + attributeName + " = :value";
            Query<T> query = session.createQuery(hql, entityClass);
            query.setParameter("value", value);
            query.setMaxResults(1);
            return query.uniqueResult();
        } catch (Exception e) {
            System.err.println("Error finding single entity by attribute: " + attributeName + " - " + e.getMessage());
            throw new RuntimeException("Error finding single entity by attribute", e);
        }
    }

    public long countAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(*) FROM " + entityClass.getSimpleName();
            Query<Long> query = session.createQuery(hql, Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            System.err.println("Error counting entities: " + e.getMessage());
            throw new RuntimeException("Error counting entities", e);
        }
    }

    // Perbaikan: Method untuk update terpisah dari save
    public T update(T entity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            System.out.println("Entity updated successfully: " + entity);
            return entity;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error updating entity: " + e.getMessage());
            throw new RuntimeException("Error updating entity of type " + entityClass.getSimpleName(), e);
        }
    }

    // Tambahan: Method untuk cek apakah entity dengan ID tertentu ada
    public boolean existsById(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(*) FROM " + entityClass.getSimpleName() + " WHERE id = :id";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("id", id);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            System.err.println("Error checking if entity exists by id: " + id + " - " + e.getMessage());
            throw new RuntimeException("Error checking entity existence", e);
        }
    }
}