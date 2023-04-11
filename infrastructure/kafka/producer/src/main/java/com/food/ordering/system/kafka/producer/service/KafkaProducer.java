package com.food.ordering.system.kafka.producer.service;

import com.food.ordering.system.kafka.producer.exception.KafkaProducerException;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@Slf4j
@Component
public class KafkaProducer<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducerInterface<K, V> {
    private final KafkaTemplate<K, V> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topicName, K key, V message, BiConsumer<SendResult<K, V>, Throwable> callback) {
        log.info("Sending message {} to topic {}", message, topicName);
        try {
            CompletableFuture<SendResult<K, V>> kafkaResultFuture = kafkaTemplate.send(topicName, key, message);
            kafkaResultFuture.whenComplete(callback);
        } catch (KafkaException e) {
            log.error("Error when producing message {} to topic {} and the exception is {}", message,  topicName,  e.getMessage());
            throw new KafkaProducerException(e.getMessage());
        }
    }

    @PreDestroy
    public void close() {
        if(kafkaTemplate != null) {
            log.info("Closing Kafka producer");
            kafkaTemplate.destroy();
        }
    }
}

