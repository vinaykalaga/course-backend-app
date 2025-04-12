package com.course.app.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.course.app.repository.course",
        entityManagerFactoryRef = "courseEntityManagerFactory",
        transactionManagerRef = "courseTransactionManager"
)
public class CourseDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.course")
    public DataSource courseDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean courseEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {

        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect"); // ✅

        return builder
                .dataSource(courseDataSource())
                .packages(
                        "com.course.app.model.course", // ✅ your course entities
                        "com.course.app.model.auth"    // ✅ include User for enrollment
                )
                .persistenceUnit("course")
                .properties(jpaProperties)
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager courseTransactionManager(
            @Qualifier("courseEntityManagerFactory") EntityManagerFactory courseEMF) {
        return new JpaTransactionManager(courseEMF);
    }
}
