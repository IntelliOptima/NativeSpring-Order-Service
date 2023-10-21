package com.nativespring.orderservice.order;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.MatcherAssert.*;      // assertThat(), containsString(), etc.
import static org.hamcrest.Matchers.*;          // contains is(), in(), instanceOf(), has(), etc.

import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.BDDMockito.*;

import com.nativespring.orderservice.order.domain.Order;
import com.nativespring.orderservice.order.domain.OrderService;
import com.nativespring.orderservice.order.domain.OrderStatus;
import com.nativespring.orderservice.order.web.OrderController;
import com.nativespring.orderservice.order.web.OrderRequest;

import reactor.core.publisher.Mono;

@WebFluxTest(OrderController.class)
public class OrderControllerWebFluxTests {

        
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OrderService orderService;


    @Test
    void whenBookNotAvailable_thenRejectOrder() {
        var orderRequest = new OrderRequest("1234567890", 3);
        var expectedOrder = OrderService.buildRejectedOrder(orderRequest.isbn(),orderRequest.quantity());

        given(orderService.submitOrder(
            orderRequest.isbn(), orderRequest.quantity()))
            .willReturn(Mono.just(expectedOrder));

        webTestClient
            .post()
            .uri("/orders")
            .bodyValue(orderRequest)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(Order.class).value(actualOrder -> {
                assertThat(actualOrder, isNotNull());
                assertThat(actualOrder.status(), is(OrderStatus.REJECTED) );
            });
    }
}
