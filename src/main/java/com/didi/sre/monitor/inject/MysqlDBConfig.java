package com.didi.sre.monitor.inject;

import java.beans.PropertyVetoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Created by soarpenguin on 17-9-9.
 */
@Configuration
public class MysqlDBConfig {
    @Autowired
    private Environment env;

    @Bean(name="dataSource")
    public ComboPooledDataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(env.getProperty("spring.db.driverClassName"));
        dataSource.setJdbcUrl(env.getProperty("spring.db.url"));
        dataSource.setUser(env.getProperty("spring.db.username"));
        dataSource.setPassword(env.getProperty("spring.db.password"));
        dataSource.setMaxPoolSize(20);
        dataSource.setMinPoolSize(5);
        dataSource.setInitialPoolSize(10);
        dataSource.setMaxIdleTime(300);
        dataSource.setAcquireIncrement(5);
        dataSource.setIdleConnectionTestPeriod(60);
        return dataSource;
    }
}
