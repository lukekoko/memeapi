package com.lukekoko.memeapi;

import com.lukekoko.memeapi.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
@EnableJpaRepositories
@EnableCaching
@EnableScheduling
public class MemeapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemeapiApplication.class, args);
    }
}
