package com.food.ordering.system.order.service.domain.usecase.createorder;

import com.food.ordering.system.application.ports.input.command.CommandHandlerInterface;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisherInterface;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderCommandHandler implements CommandHandlerInterface<CreateOrderResponse, CreateOrderCommand> {

    private final CreateOrderHelper createOrderHelper;
    private final OrderCreatedPaymentRequestMessagePublisherInterface messagePublisher;
    private final OrderDataMapper orderDataMapper;

    public CreateOrderCommandHandler(
            CreateOrderHelper createOrderHelper,
            OrderCreatedPaymentRequestMessagePublisherInterface messagePublisher,
            OrderDataMapper orderDataMapper
    ) {
        this.createOrderHelper = createOrderHelper;
        this.messagePublisher = messagePublisher;
        this.orderDataMapper = orderDataMapper;
    }

    @Override
    public CreateOrderResponse handle(CreateOrderCommand command) {
        OrderCreatedEvent orderCreatedEvent = createOrderHelper.persistOrder(command);
        messagePublisher.publish(orderCreatedEvent);
        return orderDataMapper.createOrderResponseFromOrderInstance(
                orderCreatedEvent.getEntity(),
                "Order created successfully"
        );
    }
}
