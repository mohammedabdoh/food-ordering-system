package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationServiceInterface;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepositoryInterface;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepositoryInterface;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepositoryInterface;
import com.food.ordering.system.order.service.domain.usecase.createorder.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.usecase.createorder.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.usecase.createorder.OrderAddress;
import com.food.ordering.system.order.service.domain.usecase.createorder.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
public class OrderApplicationServiceTest {

    @Autowired
    private OrderApplicationServiceInterface orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepositoryInterface orderRepository;

    @Autowired
    private CustomerRepositoryInterface customerRepository;

    @Autowired
    private RestaurantRepositoryInterface restaurantRepository;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWithWrongPrice;
    private CreateOrderCommand createOrderCommandWithWrongProductPrice;

    private final UUID CUSTOMER_ID = UUID.fromString("74cd39eb-df67-4372-a976-255328ae42df");
    private final UUID RESTAURANT_ID = UUID.fromString("5d2031e9-af20-463d-a3a4-b66f14b14bbe");
    private final UUID PRODUCT_ID = UUID.fromString("b886d665-ba35-44cc-a3d5-460459e6e0a9");
    private final UUID ORDER_ID = UUID.fromString("7c6ce8eb-6888-4114-a792-7af06453ccd9");

    private final BigDecimal PRICE = new BigDecimal("200.00");

    @BeforeEach
    public void init() {
        createOrderCommand = createACreateOrderCommandInstance(PRICE, "50.00");
        createOrderCommandWithWrongPrice = createACreateOrderCommandInstance(new BigDecimal("250.00"), "50.00");
        createOrderCommandWithWrongProductPrice = createACreateOrderCommandInstance(new BigDecimal("210.00"), "60.00");

        Customer customer = createACustomerInstance(CUSTOMER_ID);
        Restaurant restaurant = createARestaurantInstance(RESTAURANT_ID, PRODUCT_ID, true);
        Order order = createOrderInstance(createOrderCommand, ORDER_ID);

        when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(restaurantRepository.findRestaurantInformation(orderDataMapper.createRestaurantInstanceFromCreateOrderCommand(createOrderCommand))).thenReturn(Optional.of(restaurant));
        when(orderRepository.saveOrder(any(Order.class))).thenReturn(order);
    }

    @Test
    public void testItCreatesAnOrder() {
        // Act
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);

        // Assert
        assertEquals(OrderStatus.PENDING, createOrderResponse.getOrderStatus());
        assertEquals("Order created successfully", createOrderResponse.getMessage());
        assertNotNull(createOrderResponse.getOrderTrackingId());
    }

    @Test
    public void testItDoesNotCreateOrderIfTotalOrderPriceDoesNotMatchOrderItemsTotal() {
        // Assert
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class, () -> orderApplicationService.createOrder(createOrderCommandWithWrongPrice));
        assertEquals("Total price 250.00 is not equal to Order items total: 200.00", orderDomainException.getMessage());
    }

    @Test
    public void testItDoesNotCreateOrderIfOrderItemPriceIsInvalid() {
        // Assert
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class, () -> orderApplicationService.createOrder(createOrderCommandWithWrongProductPrice));
        assertEquals("Order item price 60.00 is not valid for product " + PRODUCT_ID, orderDomainException.getMessage());
    }

    @Test
    public void testItDoesNotCreateOrderIfRestaurantIsInactive() {
        Restaurant restaurant = createARestaurantInstance(RESTAURANT_ID, PRODUCT_ID, false);
        when(restaurantRepository.findRestaurantInformation(
                orderDataMapper.createRestaurantInstanceFromCreateOrderCommand(createOrderCommand)
        )).thenReturn(Optional.of(restaurant));
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class, () -> orderApplicationService.createOrder(createOrderCommand));
        assertEquals("Restaurant with id " + RESTAURANT_ID + " is not active", orderDomainException.getMessage());
    }

    private CreateOrderCommand createACreateOrderCommandInstance(BigDecimal price, String orderItemPrice) {
        return CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .orderAddress(
                        OrderAddress.builder()
                                .street("street_no_1")
                                .postalCode("JK7888")
                                .city("Alexandria")
                                .build()
                )
                .price(price)
                .orderItems(
                        List.of(
                                OrderItem.builder()
                                        .productId(PRODUCT_ID)
                                        .quantity(1)
                                        .price(new BigDecimal(orderItemPrice))
                                        .subTotal(new BigDecimal(orderItemPrice))
                                        .build(),
                                OrderItem.builder()
                                        .productId(PRODUCT_ID)
                                        .quantity(3)
                                        .price(new BigDecimal("50.00"))
                                        .subTotal(new BigDecimal("150.00"))
                                        .build()
                        )
                )
                .build();
    }

    private Restaurant createARestaurantInstance(UUID restaurantId, UUID productID, boolean active) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantId))
                .products(List.of(
                        new Product(new ProductId(productID), "product-1", new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(productID), "product-2", new Money(new BigDecimal("50.00")))
                ))
                .active(active)
                .build();
    }

    private Customer createACustomerInstance(UUID customerId) {
        Customer customer = new Customer();
        customer.setId(new CustomerId(customerId));
        return customer;
    }

    private Order createOrderInstance(CreateOrderCommand createOrderCommand, UUID orderId) {
        Order order = orderDataMapper.createOrderInstanceFromCreateOrderCommand(createOrderCommand);
        order.setId(new OrderId(orderId));
        return order;
    }
}
