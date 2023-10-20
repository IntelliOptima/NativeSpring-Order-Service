package com.nativespring.orderservice.book;


import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.RequiredArgsConstructor;

/**
 * This class is a client for the Catalog Service. It uses the WebClient to make
 * HTTP calls to the Catalog Service. The WebClient is configured to use the
 * Catalog Service URI.
 */
@Component
@RequiredArgsConstructor
public class BookClient {
    
    private static final String BOOKS_ROOT_API = "/books/";
    private final WebClient webClient;

    /**
     * WebClient is a reactive HTTP client. This is how it can return data
     * as reactive publishers. In particular, the result of calling Catalog Service to fetch
     * details about a specific book is a Mono<Book> object.
     * 
     * @param isbn
     * @return a Mono<Book> object.
     */
    public Mono<Book> getBookByIsbn(String isbn) {
        return webClient
                .get()
                .uri(BOOKS_ROOT_API + isbn)
                .retrieve()
                .bodyToMono(Book.class)
                .timeout(Duration.ofSeconds(3), Mono.empty())
                .onErrorResume(WebClientResponseException.NotFound.class, exception -> Mono.empty()) // If the book is not found, return an empty Mono with a callback
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100))) // Adding retryWhen after timeout ensures that the retry happens for every timeout
                .onErrorResume(Exception.class, exception -> Mono.empty());
    }
}
