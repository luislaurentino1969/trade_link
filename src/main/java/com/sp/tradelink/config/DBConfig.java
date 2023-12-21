package com.sp.tradelink.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConfig {

//    @Value("${spring.datasource.driver-class-name}")
//    private String driverName;
//    @Value("${spring.datasource.url}")
//    private String dbUrl;
//    @Value("${spring.datasource.username}")
//    private String dbUserName;
//    @Value("${spring.datasource.password}")
//    private String dbUserPwd;

//    @Bean
//    public DataSource getDatasource() {
//        DataSourceBuilder<?> dsBuilder = DataSourceBuilder.create();
//        dsBuilder.driverClassName(driverName);
//        dsBuilder.url(dbUrl);
//        dsBuilder.username(dbUserName);
//        dsBuilder.password(dbUserPwd);
//        return dsBuilder.build();
//    }

//    @Bean
//    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(emf);
//        return transactionManager;
//    }

//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource)
//            throws IllegalArgumentException {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setPersistenceUnitName("TradeLinkDB");
//        em.setDataSource(dataSource);
//        em.setPackagesToScan("com.sp.tradelink.models.*");
//
//        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        em.setJpaVendorAdapter(vendorAdapter);
//        em.setJpaProperties(additionalProperties());
//        return em;
//    }
//
//    Properties additionalProperties() {
//        Properties properties = new Properties();
//        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
//        properties.setProperty("spring.jpa.show-sql", "true");
//        properties.setProperty("spring.jpa.properties.hibernate.format_sql", "true");
//        return properties;
//    }
}
