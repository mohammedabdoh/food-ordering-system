package com.food.ordering.system.order.service.domain.usecase.createorder;

import com.food.ordering.system.order.service.domain.OrderDomainServiceInterface;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepositoryInterface;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepositoryInterface;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepositoryInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class CreateOrderHelper {
    private final OrderDomainServiceInterface orderDomainService;
    private final OrderRepositoryInterface orderRepository;
    private final CustomerRepositoryInterface customerRepository;
    private final RestaurantRepositoryInterface restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    public CreateOrderHelper(OrderDomainServiceInterface orderDomainService, OrderRepositoryInterface orderRepository, CustomerRepositoryInterface customerRepository, RestaurantRepositoryInterface restaurantRepository, OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());

        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderInstanceFromCreateOrderCommand(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);

        saveOrder(order);

        return orderCreatedEvent;
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);

        if(customer.isEmpty()) {
            String message = String.format("Couldn't find customer with id: %s", customerId.toString());
            log.error(message);
            throw new OrderDomainException(message);
        }
    }

    private Restaurant checkRestaurant(CreateOrderCommand command) {
        Restaurant restaurant = orderDataMapper.createRestaurantInstanceFromCreateOrderCommand(command);
        Optional<Restaurant> foundRestaurant = restaurantRepository.findRestaurantInformation(restaurant);

        if(foundRestaurant.isEmpty()) {
            String message = String.format("Couldn't find restaurant with id: %s", restaurant.getId().getValue());
            log.error(message);
            throw new OrderDomainException(message);
        }

        return foundRestaurant.get();
    }

    private void saveOrder(Order order) {
        Order savedOrder = orderRepository.saveOrder(order);
        if(savedOrder == null) {
            String message = "Order could not be saved";
            log.error(message);
            throw new OrderDomainException(message);
        }

        log.info("Order has been saved successfully");
    }
}
