package com.example.kptc_smp.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TransactionManagerConfig {

    @Bean(name = "chainedTransactionManager")
    public ChainedTransactionManager transactionManager(
            @Qualifier("postgreSQLTransactionManager") PlatformTransactionManager postgreSQLTransactionManager,
            @Qualifier("mySQLTransactionManager") PlatformTransactionManager mySQLTransactionManager) {
        return new ChainedTransactionManager(mySQLTransactionManager, postgreSQLTransactionManager);
    }
}
