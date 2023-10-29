package com.cognizant.fse.assignment.touristmanagementapp.service;

import com.cognizant.fse.assignment.touristmanagementapp.domain.TouristCompany;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

/**
 * MessageProducerService class should be used to send messages to Kafka Topic
 */
@Service
@RequiredArgsConstructor
public class MessageProducerService {

    private static final String QUEUE_NAME = "yuva-fse-service-bus-queue";

    /**
     * Inject a bean of KafkaTemplate created in KafkaConfig class
     *
     */

    @Autowired
    private final JmsTemplate jmsTemplate;

    public void sendOrderMessage(TouristCompany touristCompany) throws ExecutionException, InterruptedException {
        jmsTemplate.convertAndSend(QUEUE_NAME, touristCompany);
    }
}