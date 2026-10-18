package com.wipro.employeemanagement.dao;

import com.wipro.employeemanagement.model.User;
import com.wipro.employeemanagement.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 * Data Access Object (DAO) for managing User entity persistence with Hibernate 6.
 */
public class UserDAO {

    /**
     * Persists a new User in the database inside a Hibernate transaction.
     *
     * @param user The User entity to save
     * @throws Exception if persistence fails or constraint violation occurs
     */
    public void saveUser(User user) throws Exception {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    /**
     * Checks if a user already exists with the given employee number.
     *
     * @param employeeNumber Employee number to search for (case-insensitive)
     * @return true if exists, false otherwise
     */
    public boolean isEmployeeNumberExists(String employeeNumber) {
        if (employeeNumber == null || employeeNumber.trim().isEmpty()) {
            return false;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(u) FROM User u WHERE UPPER(u.employeeNumber) = UPPER(:empNo)";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("empNo", employeeNumber.trim());
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            System.err.println("[UserDAO] Error checking employee number existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a user already exists with the given email address.
     *
     * @param email Email address to search for (case-insensitive)
     * @return true if exists, false otherwise
     */
    public boolean isEmailExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(u) FROM User u WHERE LOWER(u.email) = LOWER(:email)";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("email", email.trim());
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            System.err.println("[UserDAO] Error checking email existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a User entity by employee number.
     *
     * @param employeeNumber Employee number
     * @return User object or null if not found
     */
    public User getUserByEmployeeNumber(String employeeNumber) {
        if (employeeNumber == null || employeeNumber.trim().isEmpty()) {
            return false ? null : null;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User u WHERE UPPER(u.employeeNumber) = UPPER(:empNo)";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("empNo", employeeNumber.trim());
            return query.uniqueResult();
        } catch (Exception e) {
            System.err.println("[UserDAO] Error retrieving user by employee number: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves a User entity by employee number or email address (case-insensitive).
     *
     * @param identifier Employee number or email
     * @return User object or null if not found
     */
    public User getUserByUsernameOrEmployeeNumber(String identifier) {
        if (identifier == null || identifier.trim().isEmpty()) {
            return null;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User u WHERE UPPER(u.employeeNumber) = UPPER(:id) OR LOWER(u.email) = LOWER(:id)";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("id", identifier.trim());
            return query.uniqueResult();
        } catch (Exception e) {
            System.err.println("[UserDAO] Error retrieving user by identifier: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves a User entity by its primary key ID.
     *
     * @param id User ID
     * @return User object or null if not found
     */
    public User getUserById(Long id) {
        if (id == null) {
            return null;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        } catch (Exception e) {
            System.err.println("[UserDAO] Error retrieving user by id: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing User's details inside a Hibernate transaction.
     *
     * @param user User entity to update
     * @throws Exception if transaction fails
     */
    public void updateUser(User user) throws Exception {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
