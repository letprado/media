package com.project.media.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("!test")
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean
    @Primary
    public DataSource dataSource() {
        logger.info("Configurando DataSource otimizado para Oracle FIAP - FECHA CONEXÕES RAPIDAMENTE");

        HikariConfig config = new HikariConfig();
        
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);
        config.setMaximumPoolSize(1);
        config.setMinimumIdle(0);
        config.setConnectionTimeout(3000);
        config.setIdleTimeout(5000);
        config.setMaxLifetime(30000);
        config.setValidationTimeout(2000);
        config.setKeepaliveTime(0);
        config.setLeakDetectionThreshold(10000);
        config.setInitializationFailTimeout(-1);
        config.setAutoCommit(true);
        config.setConnectionTestQuery("SELECT 1 FROM DUAL");
        config.setPoolName("OraclePoolFIAP");
        config.setRegisterMbeans(true);

        logger.info("HikariCP configurado: max=1, idle=0, maxLifetime=30s, idleTimeout=5s");
        
        return new HikariDataSource(config);
    }
}

