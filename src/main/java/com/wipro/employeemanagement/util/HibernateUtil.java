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

                // 2. Load custom database properties from db.properties if present
                Properties dbProps = new Properties();
                try (InputStream input = HibernateUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
                    if (input != null) {
                        dbProps.load(input);
                    }
                } catch (Exception e) {
                    System.err.println("[HibernateUtil] Note reading db.properties: " + e.getMessage());
                }

                // 2a. Apply database configuration: Environment variables take precedence over db.properties
                String driver = getEnvOrProp("DB_DRIVER", dbProps.getProperty("db.driver"));
                if (driver != null && !driver.isEmpty()) {
                    registryBuilder.applySetting(Environment.JAKARTA_JDBC_DRIVER, driver);
                    registryBuilder.applySetting("hibernate.connection.driver_class", driver);
                }

                String url = getEnvOrProp("DB_URL", getEnvOrProp("DATABASE_URL", getEnvOrProp("MYSQL_URL", dbProps.getProperty("db.url"))));
                if (url != null && !url.isEmpty()) {
                    registryBuilder.applySetting(Environment.JAKARTA_JDBC_URL, url);
                    registryBuilder.applySetting("hibernate.connection.url", url);
                }

                String user = getEnvOrProp("DB_USERNAME", getEnvOrProp("MYSQL_USER", dbProps.getProperty("db.username")));
                if (user != null && !user.isEmpty()) {
                    registryBuilder.applySetting(Environment.JAKARTA_JDBC_USER, user);
                    registryBuilder.applySetting("hibernate.connection.username", user);
                }

                String pass = getEnvOrProp("DB_PASSWORD", getEnvOrProp("MYSQL_PASSWORD", dbProps.getProperty("db.password")));
                if (pass != null) {
                    registryBuilder.applySetting(Environment.JAKARTA_JDBC_PASSWORD, pass);
                    registryBuilder.applySetting("hibernate.connection.password", pass);
                }

                String dialect = getEnvOrProp("DB_DIALECT", dbProps.getProperty("db.dialect"));
                if (dialect != null && !dialect.isEmpty()) {
                    registryBuilder.applySetting(Environment.DIALECT, dialect);
                    registryBuilder.applySetting("hibernate.dialect", dialect);
                }

                String showSql = getEnvOrProp("DB_SHOW_SQL", dbProps.getProperty("db.show_sql"));
                if (showSql != null && !showSql.isEmpty()) {
                    registryBuilder.applySetting(Environment.SHOW_SQL, showSql);
                    registryBuilder.applySetting("hibernate.show_sql", showSql);
                }

                String formatSql = getEnvOrProp("DB_FORMAT_SQL", dbProps.getProperty("db.format_sql"));
                if (formatSql != null && !formatSql.isEmpty()) {
                    registryBuilder.applySetting(Environment.FORMAT_SQL, formatSql);
                    registryBuilder.applySetting("hibernate.format_sql", formatSql);
                }

                String hbm2ddl = getEnvOrProp("DB_HBM2DDL_AUTO", dbProps.getProperty("db.hbm2ddl.auto"));
                if (hbm2ddl != null && !hbm2ddl.isEmpty()) {
                    registryBuilder.applySetting(Environment.HBM2DDL_AUTO, hbm2ddl);
                    registryBuilder.applySetting("hibernate.hbm2ddl.auto", hbm2ddl);
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

    /**
     * Reads a value from Environment variables, falling back to System properties, then the fallback value.
     */
    private static String getEnvOrProp(String key, String fallback) {
        String val = System.getenv(key);
        if (val != null && !val.trim().isEmpty()) {
            return val.trim();
        }
        val = System.getProperty(key);
        if (val != null && !val.trim().isEmpty()) {
            return val.trim();
        }
        return fallback;
    }
}
