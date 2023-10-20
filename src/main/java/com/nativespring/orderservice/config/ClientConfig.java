package com.nativespring.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {

    /**
     * Creates a {@link WebClient} bean that is configured to use the catalog
     * service URI.
     * 
     * @param clientProperties
     * @return a {@link WebClient} bean that is configured to use the catalog
     *         service URI.
     */
    @Bean
    WebClient webClient(ClientProperties clientProperties, WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(clientProperties.catalogServiceUri().toString())
                .build();
    }
}
