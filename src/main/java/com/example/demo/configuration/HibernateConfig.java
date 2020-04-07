//package com.example.demo.configuration;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
//@Configuration
//@EnableTransactionManagement
//@PropertySource("classpath:application.properties")
//public class HibernateConfig {
//    private String dbUrl, dbUsername, dbPassword;
//
//    @Autowired
//    public HibernateConfig(Environment env) {
//        dbUrl = env.getProperty("database.url");
//        dbUsername = env.getProperty("database.username");
//        dbPassword = env.getProperty("database.password");
//    }
//
//    @Bean
//    public LocalSessionFactoryBean sessionFactory() {
//        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//        sessionFactory.setDataSource(dataSource());
//        sessionFactory.setPackagesToScan("com.example.demo.models");
//        sessionFactory.setHibernateProperties(hibernateProperties());
//        return sessionFactory;
//    }
//
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl(dbUrl);
//        dataSource.setUsername(dbUsername);
//        dataSource.setPassword(dbPassword);
//        return dataSource;
//    }
//
//    private Properties hibernateProperties() {
//        Properties hibernateProperties = new Properties();
//        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
//        //hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
//        return hibernateProperties;
//    }
//}
