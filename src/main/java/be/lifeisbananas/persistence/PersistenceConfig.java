package be.lifeisbananas.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.*;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@Configuration // Zegt tegen Spring: "Hier staan de instellingen"
@ComponentScan("be.lifeisbananas") // Zoek naar alle @Service en @Repository stickers in dit project
@EnableTransactionManagement // ZET DE TRANSACTIES (@Transactional) AAN!
public class PersistenceConfig {

    // 1. De Database Verbinding (De kabel naar XAMPP)
    @Bean
    public BasicDataSource datasource(){
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/life_is_bananas_db");
        ds.setUsername("root"); // Standaard XAMPP login
        ds.setPassword(""); // Standaard XAMPP wachtwoord (leeg)
        ds.setInitialSize(5);
        ds.setMaxTotal(10);
        // ISO normen (Integrity):
        ds.setDefaultTransactionIsolation(java.sql.Connection.TRANSACTION_SERIALIZABLE);
        return ds;
    }

    // 2. De Hibernate Motor (Vertaalt Java naar SQL)
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sf = new LocalSessionFactoryBean();
        sf.setDataSource(datasource());
        sf.setPackagesToScan("be.lifeisbananas");

        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

        // Dit vertelt Hibernate om de tabellen ZELF te bouwen in de lege database!
        hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
        hibernateProperties.put("hibernate.show_sql", "true"); // Laat de SQL zien in je console

        sf.setHibernateProperties(hibernateProperties);
        return sf;
    }

    // 3. De Scheidsrechter (Regelt Commit en Rollback)
    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory().getObject());
        return txManager;
    }
}