package com.project.media.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("oracle")
public class OracleDataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(OracleDataInitializer.class);

    @Override
    public void run(String... args) throws Exception {
        logger.info("=== PERFIL ORACLE ATIVO ===");
        logger.info("DataInitializer DESABILITADO - dados devem ser inseridos via SQL manual");
        logger.info("Execute o script oracle-setup.sql no banco antes de usar a API");
    }
}

