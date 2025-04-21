package com.amnex.fr_farmer_farm_sync.configuration.liquibase;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseMultiDBConfig {

    @Value("${spring.primary.liquibase.change-log}")
    private String liquibasePath;

    @Value("${spring.primary.liquibase.database-change-log-table}")
    private String changeLogTable;

    @Value("${spring.primary.liquibase.database-change-log-lock-table}")
    private String changeLogLockTable;

    @Value("${spring.primary.liquibase.default-schema}")
    private String defaultSchema;

    @Value("${spring.primary.liquibase.drop-first}")
    private Boolean dropFirst;

    @Value("${spring.primary.liquibase.enabled}")
    private Boolean liquibaseEnable;

    @Bean(name = "liquibasePrimary")
    public SpringLiquibase liquibasePrimary(@Qualifier("primaryDataSource") DataSource primaryDataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(primaryDataSource);
        liquibase.setChangeLog(liquibasePath);
        liquibase.setDatabaseChangeLogTable(changeLogTable);
        liquibase.setDatabaseChangeLogLockTable(changeLogLockTable);
        liquibase.setDefaultSchema(defaultSchema);
        liquibase.setDropFirst(dropFirst);
        liquibase.setShouldRun(liquibaseEnable);
        return liquibase;
    }
}
