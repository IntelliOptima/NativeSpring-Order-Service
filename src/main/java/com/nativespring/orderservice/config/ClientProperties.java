package com.nativespring.orderservice.config;

import java.net.URI;

import jakarta.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "polar")
public record ClientProperties (

    /**
     * The URI of the catalog service.
     */
	@NotNull
	URI catalogServiceUri

){}
