package org.example.kundtjanst.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Configurera rest client
 * Denna behövs för att vi kan göra HTTP-anrop mot andra tjänster
 */
@Configuration
public class RestClientConfig
{
    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }
}
