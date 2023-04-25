package com.devinsight.lab4.databaseelab4solution;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class DatabaseCredentials {
    @Value("${database.password}")
    private String password;

    @Value("${database.server}")
    private String url;

    @Value("${database.username}")
    private String username;
}
