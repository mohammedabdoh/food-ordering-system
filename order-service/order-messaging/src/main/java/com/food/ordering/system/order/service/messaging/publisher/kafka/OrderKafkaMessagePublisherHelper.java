package com.food.ordering.system.order.service.messaging.publisher.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
public class OrderKafkaMessagePublisherHelper {

    public <T> BiConsumer<SendResult<String, T>, Throwable>
    createKafkaCallback(String topicName, T message, String orderId)
    {
        return (stringPaymentRequestAvroModelSendResult, throwable) -> {
            if(!throwable.getMessage().isEmpty()) {
                log.error("Error sending the message {} to topic {}", message, topicName);
            } else {
                RecordMetadata recordMetadata = stringPaymentRequestAvroModelSendResult.getRecordMetadata();
                log.info(
                        "Published the message for order id {} to topic {} in partition {} with offset {} and timestamp {}",
                        orderId,
                        recordMetadata.topic(),
                        recordMetadata.partition(),
                        recordMetadata.offset(),
                        recordMetadata.timestamp()
                );
            }
        };
    }
}
