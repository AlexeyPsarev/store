package com.dataart.springtraining.config;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
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
@EnableJpaRepositories("com.dataart.springtraining.dao")
public class PersistenceJPAConfig
{
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory()
	{
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource());
		emf.setPackagesToScan("com.dataart.springtraining.domain");
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		emf.setJpaVendorAdapter(vendorAdapter);
		emf.setJpaProperties(additionalProperties());
		return emf;
	}
		
	@Bean
	public DataSource dataSource()
	{
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:~/PUBLIC;AUTO_SERVER=TRUE;AUTO_SERVER_PORT=8082;SCHEMA=PUBLIC;DB_CLOSE_ON_EXIT=FALSE");
		dataSource.setUsername("admin");
		dataSource.setPassword("admin");
		return dataSource;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf)
	{
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}
	
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation()
	{
		return new PersistenceExceptionTranslationPostProcessor();
	}
	
	Properties additionalProperties()
	{
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		return properties;
	}
}
