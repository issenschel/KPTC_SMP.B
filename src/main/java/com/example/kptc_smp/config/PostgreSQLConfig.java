package com.example.kptc_smp.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "postgreSQLEntityMangerFactoryBean", basePackages = {
        "com.example.kptc_smp.repository.postgreSQL" }, transactionManagerRef = "postgreSQLTransactionManager")
public class PostgreSQLConfig {

    @Autowired
    private Environment environment;

    @Bean(name = "postgreSQLDataSource")
    @Primary
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(environment.getProperty("postgreSQL.datasource.url"));
        dataSource.setDriverClassName(environment.getProperty("mySQL.datasource.driver-class-name"));
        dataSource.setUsername(environment.getProperty("postgreSQL.datasource.username"));
        dataSource.setPassword(environment.getProperty("postgreSQL.datasource.password"));
        return dataSource;
    }

    @Primary
    @Bean(name = "postgreSQLEntityMangerFactoryBean")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource());
        bean.setPackagesToScan("com.example.kptc_smp.entity.postgreSQL");

        JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        bean.setJpaVendorAdapter(adapter);

        Map<String, String> props = new HashMap<>();
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "update");
        bean.setJpaPropertyMap(props);

        return bean;
    }

    @Primary
    @Bean(name = "postgreSQLTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("postgreSQLEntityMangerFactoryBean") EntityManagerFactory entityManagerFactory ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
