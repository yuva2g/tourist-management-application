package com.cognizant.fse.assignment.touristmanagementapp.service;

import com.cognizant.fse.assignment.touristmanagementapp.domain.TouristCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

/**
 * MessageProducerService class should be used to send messages to Kafka Topic
 */
@Service
public class MessageProducerService {

    @Value("${kafka.topic-name}")
    private String topic;

    /**
     * Inject a bean of KafkaTemplate created in KafkaConfig class
     *
     */
    KafkaTemplate<String, TouristCompany> kafkaTemplate;

    @Autowired
    public MessageProducerService(KafkaTemplate<String, TouristCompany> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public TouristCompany sendOrderMessage(TouristCompany orderDto) throws ExecutionException, InterruptedException {
        ListenableFuture<SendResult<String, TouristCompany>> result = kafkaTemplate.send(topic, orderDto);
        return result.get().getProducerRecord().value();
    }
}