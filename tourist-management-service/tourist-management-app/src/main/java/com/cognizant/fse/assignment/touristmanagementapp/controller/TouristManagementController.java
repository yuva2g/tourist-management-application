package com.cognizant.fse.assignment.touristmanagementapp.controller;

import com.cognizant.fse.assignment.touristmanagementapp.domain.TouristCompany;
import com.cognizant.fse.assignment.touristmanagementapp.repository.TouristCompanyRepository;
import com.cognizant.fse.assignment.touristmanagementapp.service.MessageProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/tourism/api/v1/branch")
@RequiredArgsConstructor
@Slf4j
public class TouristManagementController {

    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private final TouristCompanyRepository touristCompanyRepository;
    private final MessageProducerService producer;

    @PostMapping("/add-places")
    public TouristCompany createTouristCompany(@Valid @RequestBody TouristCompany touristCompany) {
        if (!touristCompany.getWebsite().contains("www")) {
            throw new IllegalArgumentException("Website should contain 'www'");
        }

        if (!isValidEmail(touristCompany.getEmail())) {
            throw new IllegalArgumentException("Invalid email");
        }

        if (!isValidMobile(touristCompany.getContact())) {
            throw new IllegalArgumentException("Invalid mobile number");
        }

        TouristCompany savedTouristCompany = touristCompanyRepository.save(touristCompany);
        try {
            producer.sendOrderMessage(savedTouristCompany);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error while producing new order message");
        }
        return savedTouristCompany;
    }

    @PutMapping("/update-tariff/{branchId}")
    public TouristCompany updateTariffDetails(@PathVariable Long branchId, @RequestBody TouristCompany updatedCompany) {
        TouristCompany existingCompany = touristCompanyRepository.findById(branchId)
                .orElseThrow(() -> new IllegalArgumentException("Branch ID not found"));

        // Check if updatedCompany contains valid tariff details
        isValidTariffDetails(updatedCompany);

        // Update only the tariff details
        updatedCompany.getTouristPlaces().forEach(updatedPlace -> {
            existingCompany.getTouristPlaces().forEach(existingPlace -> {
                if (updatedPlace.getId().equals(existingPlace.getId())) {
                    existingPlace.setTariff(updatedPlace.getTariff());
                }
            });
        });

        TouristCompany savedTouristCompany = touristCompanyRepository.save(existingCompany);
        try {
            producer.sendOrderMessage(savedTouristCompany);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error while producing new order message");
        }
        return savedTouristCompany;
    }

    private void isValidTariffDetails(TouristCompany company) {
        company.getTouristPlaces().forEach(updatedPlace -> {
            if (updatedPlace.getTariff() < 50000 || updatedPlace.getTariff() > 100000) {
                throw new IllegalArgumentException("Invalid tariff details");
            }
        });
    }

    @GetMapping
    public List<TouristCompany> getAllTouristCompanies() {
        return touristCompanyRepository.findAll();
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidMobile(String mobile) {
        return mobile.matches("\\d{10}");
    }
}