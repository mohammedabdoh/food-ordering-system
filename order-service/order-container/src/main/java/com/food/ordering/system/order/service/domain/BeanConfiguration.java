package com.food.ordering.system.order.service.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public OrderDomainServiceInterface orderDomainService() {
        return new OrderDomainService();
    }
}
