package com.nativespring.orderservice.order.domain;


import org.springframework.stereotype.Service;

import com.nativespring.orderservice.book.Book;
import com.nativespring.orderservice.book.BookClient;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final BookClient bookClient;
    private final OrderRepository orderRepository;

    // Flux is used within the reactive programming model
    public Flux<Order> getAllOrders() {         // Flux is a stream of 0 or more elements (in this case, Order objects)
        return orderRepository.findAll();
    }

    public Mono<Order> submitOrder(String isbn, int quantity) {
        return bookClient.getBookByIsbn(isbn)
            .map(book -> buildAcceptedOrder(book, quantity))
            .defaultIfEmpty(
                buildRejectedOrder(isbn, quantity)
            )
            .flatMap(orderRepository::save);
    }

    public static Order buildAcceptedOrder(Book book, int quantity) {
        return Order.of(book.isbn(), book.title() + " - " + book.author(), book.price(), quantity, OrderStatus.ACCEPTED);
    }

    public static Order buildRejectedOrder(String bookIsbn, int quantity) {
        return Order.of(bookIsbn, null, null, quantity, OrderStatus.REJECTED);
    }
}
