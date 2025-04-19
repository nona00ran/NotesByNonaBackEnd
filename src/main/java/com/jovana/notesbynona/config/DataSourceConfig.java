package com.jovana.notesbynona.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final HikariDataSource dataSource;

    @PreDestroy
    public void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
