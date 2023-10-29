package com.cognizant.fse.assignment.touristmanagementqueryapp.component;

import com.cognizant.fse.assignment.touristmanagementqueryapp.domain.TouristCompany;
import com.cognizant.fse.assignment.touristmanagementqueryapp.repository.TouristCompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * MessageProducerService class should be used to receive messages from Kafka Topic
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageReceiverService {

    private final TouristCompanyRepository touristCompanyRepository;
    private static final String QUEUE_NAME = "yuva-fse-service-bus-queue";

    @JmsListener(destination = "", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(TouristCompany message) {

        log.info("Tourist Company with id: {} received successfully", message.getId());
        touristCompanyRepository.save(message);
    }
}
