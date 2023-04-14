package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurant.RestaurantApprovalMessageListenerInterface;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurant.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {
    private final RestaurantApprovalMessageListenerInterface restaurantApprovalMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public RestaurantApprovalResponseKafkaListener(
            RestaurantApprovalMessageListenerInterface restaurantApprovalMessageListener,
            OrderMessagingDataMapper orderMessagingDataMapper) {
        this.restaurantApprovalMessageListener = restaurantApprovalMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
        topics = "${order-service.restaurant-approval-response-topic-name}"
    )
    public void consume(
            @Payload  List<RestaurantApprovalResponseAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
            @Header(KafkaHeaders.PARTITION) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets)
    {
        log.info(
                "{} number of restaurant approval messages received with keys {}, partitions {}, and, offsets {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString()
        );

        messages.forEach(message -> {
            RestaurantApprovalResponse restaurantApprovalResponse = orderMessagingDataMapper.restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(message);
            if(OrderApprovalStatus.APPROVED == message.getOrderApprovalStatus()) {
                log.info("Processing approved for order id: {}", message.getOrderId());
                restaurantApprovalMessageListener.orderApproved(restaurantApprovalResponse);
            } else if(OrderApprovalStatus.REJECTED == message.getOrderApprovalStatus()) {
                log.info("Processing rejected for order id: {}", message.getOrderId());
                restaurantApprovalMessageListener.orderRejected(restaurantApprovalResponse);
            }
        });
    }
}
