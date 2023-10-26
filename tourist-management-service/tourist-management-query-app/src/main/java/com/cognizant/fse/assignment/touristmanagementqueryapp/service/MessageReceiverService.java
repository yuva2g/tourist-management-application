package com.cognizant.fse.assignment.touristmanagementqueryapp.service;

import com.cognizant.fse.assignment.touristmanagementqueryapp.domain.TouristCompany;
import com.cognizant.fse.assignment.touristmanagementqueryapp.repository.TouristCompanyRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.concurrent.CountDownLatch;

/**
 * MessageProducerService class should be used to receive messages from Kafka Topic
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageReceiverService {

    private final TouristCompanyRepository touristCompanyRepository;

    @Getter
    private final CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = {"${kafka.topic-name}"})
    public void receive(@RequestBody TouristCompany message) {

        log.info("Tourist Company with id: {} received successfully", message.getId());
        getLatch().countDown();
        touristCompanyRepository.save(message);
    }
}
