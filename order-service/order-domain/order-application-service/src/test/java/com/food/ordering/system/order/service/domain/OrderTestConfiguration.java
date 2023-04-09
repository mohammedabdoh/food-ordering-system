package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisherInterface;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisherInterface;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurant.OrderPaidRestaurantRequestMessagePublisherInterface;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepositoryInterface;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepositoryInterface;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepositoryInterface;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.food.ordering.system")
public class OrderTestConfiguration {
    @Bean
    public OrderCreatedPaymentRequestMessagePublisherInterface orderCreatedPaymentRequestMessagePublisher() {
        return Mockito.mock(OrderCreatedPaymentRequestMessagePublisherInterface.class);
    }

    @Bean
    public OrderCancelledPaymentRequestMessagePublisherInterface orderCancelledPaymentRequestMessagePublisher() {
        return Mockito.mock(OrderCancelledPaymentRequestMessagePublisherInterface.class);
    }

    @Bean
    public OrderPaidRestaurantRequestMessagePublisherInterface orderPaidRestaurantRequestMessagePublisher() {
        return Mockito.mock(OrderPaidRestaurantRequestMessagePublisherInterface.class);
    }

    @Bean
    public OrderRepositoryInterface orderRepository() {
        return Mockito.mock(OrderRepositoryInterface.class);
    }

    @Bean
    public CustomerRepositoryInterface customerRepository() {
        return Mockito.mock(CustomerRepositoryInterface.class);
    }

    @Bean
    public RestaurantRepositoryInterface restaurantRepository() {
        return Mockito.mock(RestaurantRepositoryInterface.class);
    }

    @Bean
    public OrderDomainServiceInterface orderDomainService() {
        return new OrderDomainService();
    }
}
