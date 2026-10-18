package com.wipro.employeemanagement.util;

import java.io.InputStream;
import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

/**
 * Utility class to configure and manage the Hibernate SessionFactory singleton.
 * Designed for Hibernate ORM 6 and MySQL.
 * 
 * It dynamically loads database connection parameters from db.properties
 * to keep credentials configurable and secure.
 */
public class HibernateUtil {

    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    // Private constructor to prevent direct instantiation
    private HibernateUtil() {}

    /**
     * Builds and returns the thread-safe Hibernate SessionFactory singleton.
     *
     * @return SessionFactory instance
     */
    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // 1. Create StandardServiceRegistryBuilder and configure base settings from hibernate.cfg.xml
                StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
                try {
                    registryBuilder.configure("hibernate.cfg.xml");
                } catch (Exception e) {
                    System.out.println("[HibernateUtil] Base configuration loaded.");
                }

                // 2. Load custom database properties from db.properties if present and overlay them
                Properties dbProps = new Properties();
                try (InputStream input = HibernateUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
                    if (input != null) {
                        dbProps.load(input);

                        // Apply both Hibernate and Jakarta settings for maximum compatibility
                        if (dbProps.containsKey("db.driver")) {
                            String driver = dbProps.getProperty("db.driver").trim();
                            registryBuilder.applySetting(Environment.JAKARTA_JDBC_DRIVER, driver);
                            registryBuilder.applySetting("hibernate.connection.driver_class", driver);
                        }
                        if (dbProps.containsKey("db.url")) {
                            String url = dbProps.getProperty("db.url").trim();
                            registryBuilder.applySetting(Environment.JAKARTA_JDBC_URL, url);
                            registryBuilder.applySetting("hibernate.connection.url", url);
                        }
                        if (dbProps.containsKey("db.username")) {
                            String user = dbProps.getProperty("db.username").trim();
                            registryBuilder.applySetting(Environment.JAKARTA_JDBC_USER, user);
                            registryBuilder.applySetting("hibernate.connection.username", user);
                        }
                        if (dbProps.containsKey("db.password")) {
                            String pass = dbProps.getProperty("db.password");
                            registryBuilder.applySetting(Environment.JAKARTA_JDBC_PASSWORD, pass);
                            registryBuilder.applySetting("hibernate.connection.password", pass);
                        }
                        if (dbProps.containsKey("db.dialect")) {
                            String dialect = dbProps.getProperty("db.dialect").trim();
                            registryBuilder.applySetting(Environment.DIALECT, dialect);
                            registryBuilder.applySetting("hibernate.dialect", dialect);
                        }
                        if (dbProps.containsKey("db.show_sql")) {
                            String showSql = dbProps.getProperty("db.show_sql").trim();
                            registryBuilder.applySetting(Environment.SHOW_SQL, showSql);
                            registryBuilder.applySetting("hibernate.show_sql", showSql);
                        }
                        if (dbProps.containsKey("db.format_sql")) {
                            String formatSql = dbProps.getProperty("db.format_sql").trim();
                            registryBuilder.applySetting(Environment.FORMAT_SQL, formatSql);
                            registryBuilder.applySetting("hibernate.format_sql", formatSql);
                        }
                        if (dbProps.containsKey("db.hbm2ddl.auto")) {
                            String hbm2ddl = dbProps.getProperty("db.hbm2ddl.auto").trim();
                            registryBuilder.applySetting(Environment.HBM2DDL_AUTO, hbm2ddl);
                            registryBuilder.applySetting("hibernate.hbm2ddl.auto", hbm2ddl);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[HibernateUtil] Note reading db.properties: " + e.getMessage());
                }

                // 3. Build Registry
                registry = registryBuilder.build();

                // 4. Create MetadataSources using registry
                MetadataSources sources = new MetadataSources(registry);

                // Entity classes registered for Hibernate mapping
                sources.addAnnotatedClass(com.wipro.employeemanagement.model.User.class);
                sources.addAnnotatedClass(com.wipro.employeemanagement.model.Employee.class);

                // 5. Build Metadata and SessionFactory
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
                System.out.println("[HibernateUtil] Hibernate SessionFactory initialized successfully.");

            } catch (Exception e) {
                System.err.println("[HibernateUtil] Error initializing SessionFactory: " + e.getMessage());
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }

    /**
     * Safely closes the Hibernate SessionFactory and destroys the registry.
     */
    public static synchronized void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        System.out.println("[HibernateUtil] SessionFactory closed gracefully.");
    }
}
