package com.lukekoko.memeapi;

import com.lukekoko.memeapi.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableConfigurationProperties(AppConfig.class)
public class MemeapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemeapiApplication.class, args);
    }
}
