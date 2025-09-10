/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.config;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import za.gov.sars.common.DatasourceUtility;

@Configuration
@EnableJpaRepositories(basePackages = {"za.gov.sars.persistence"}, transactionManagerRef = "transactionManager")
@ComponentScan(basePackages = {"za.gov.sars"})
@Import({JmsConfiguration.class})
@EnableTransactionManagement
public class DataSourceConfiguration {

    @Bean(name = "localContainerEntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        Map<String, Object> hibernateProps = new LinkedHashMap<>();
        hibernateProps.put(org.hibernate.cfg.Environment.DIALECT, "org.hibernate.dialect.SQLServer2008Dialect");
        hibernateProps.put(org.hibernate.cfg.Environment.SHOW_SQL, true);
        hibernateProps.put(org.hibernate.cfg.Environment.FORMAT_SQL, true);
        hibernateProps.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "update");
        hibernateProps.put(org.hibernate.cfg.Environment.ENABLE_LAZY_LOAD_NO_TRANS, true);
        hibernateProps.put(org.hibernate.cfg.Environment.GENERATE_STATISTICS, false);
        hibernateProps.put(org.hibernate.cfg.Environment.DEFAULT_BATCH_FETCH_SIZE, 10);
        hibernateProps.put(org.hibernate.cfg.Environment.BATCH_FETCH_STYLE, "dynamic");
        hibernateProps.put(org.hibernate.cfg.Environment.ORDER_INSERTS, true);
        hibernateProps.put(org.hibernate.cfg.Environment.ORDER_UPDATES, true);
        hibernateProps.put(org.hibernate.cfg.Environment.USE_QUERY_CACHE, true);
        hibernateProps.put("org.hibernate.envers.autoRegisterListeners", true);
        hibernateProps.put("org.hibernate.envers.store_data_at_delete", true);
        hibernateProps.put("hibernate.event.merge.entity_copy_observer", "allow");
        hibernateProps.put("org.hibernate.envers.audit_table_suffix", "_AUD");
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("server-persistence-unit");
        localContainerEntityManagerFactoryBean.setPackagesToScan("za.gov.sars.domain");
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(hibernateProps);
        localContainerEntityManagerFactoryBean.setDataSource(DatasourceUtility.getDatasourceLookup());
        return localContainerEntityManagerFactoryBean;
    }

    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory(@Qualifier("localContainerEntityManagerFactoryBean") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return entityManagerFactoryBean.getObject();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = "auditReader")
    AuditReader auditReader(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return AuditReaderFactory.get(entityManagerFactory.createEntityManager());
    }

   
}
