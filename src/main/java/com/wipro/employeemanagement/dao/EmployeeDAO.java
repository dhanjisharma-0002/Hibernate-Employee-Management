package com.wipro.employeemanagement.dao;

import com.wipro.employeemanagement.model.Employee;
import com.wipro.employeemanagement.util.HibernateUtil;
import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 * Data Access Object (DAO) for Employee CRUD operations using Hibernate ORM 6.
 */
public class EmployeeDAO {

    /**
     * Persists a new Employee. ID is generated automatically by MySQL AUTO_INCREMENT.
     *
     * @param employee The employee entity to save
     * @throws Exception if persistence fails
     */
    public void saveEmployee(Employee employee) throws Exception {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(employee);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    /**
     * Retrieves an Employee by their primary key ID.
     *
     * @param id Employee ID
     * @return Employee object or null if not found
     */
    public Employee getEmployeeById(Long id) {
        if (id == null) {
            return null;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Employee.class, id);
        } catch (Exception e) {
            System.err.println("[EmployeeDAO] Error retrieving employee by id: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves all employees from the database.
     *
     * @return List of Employee records
     */
    public List<Employee> getAllEmployees() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Employee e ORDER BY e.id ASC";
            Query<Employee> query = session.createQuery(hql, Employee.class);
            return query.list();
        } catch (Exception e) {
            System.err.println("[EmployeeDAO] Error retrieving all employees: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Updates an existing Employee's record.
     *
     * @param employee The employee entity to update
     * @throws Exception if update fails
     */
    public void updateEmployee(Employee employee) throws Exception {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(employee);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    /**
     * Deletes an Employee by ID.
     *
     * @param id Employee ID
     * @return true if deleted successfully, false if not found
     * @throws Exception if deletion transaction fails
     */
    public boolean deleteEmployee(Long id) throws Exception {
        if (id == null) {
            return false;
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Employee emp = session.get(Employee.class, id);
            if (emp != null) {
                session.remove(emp);
                transaction.commit();
                return true;
            } else {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                return false;
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
