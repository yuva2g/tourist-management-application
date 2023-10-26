package com.cognizant.fse.assignment.touristmanagementqueryapp.controller;

import com.cognizant.fse.assignment.touristmanagementqueryapp.domain.TouristCompany;
import com.cognizant.fse.assignment.touristmanagementqueryapp.repository.TouristCompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/tourism/api/v1/branch")
@RequiredArgsConstructor
@Slf4j
public class TouristManagementQueryController {

    private final TouristCompanyRepository touristCompanyRepository;

    @GetMapping("/admin/{criteria}/{criteriaValue}")
    public List<TouristCompany> searchTouristCompanies(
            @PathVariable String criteria,
            @PathVariable String criteriaValue) {
        List<TouristCompany> result = new ArrayList<>();

        if (criteria.equalsIgnoreCase("branchId")) {
            // Search by Branch ID
            Long branchId = Long.parseLong(criteriaValue);
            TouristCompany company = touristCompanyRepository.findById(branchId)
                    .orElseThrow(() -> new IllegalArgumentException("Branch ID not found"));
            result.add(company);
        } else if (criteria.equalsIgnoreCase("branchName")) {
            // Search by Branch Name
            List<TouristCompany> companies = touristCompanyRepository.findByBranchName(criteriaValue);
            result.addAll(companies);
        } else if (criteria.equalsIgnoreCase("places")) {
            // Search by Place
            if (!Arrays.asList("ANDAMAN", "THAILAND", "DUBAI", "SINGAPORE", "MALAYSIA").contains(criteriaValue)) {
                throw new IllegalArgumentException("Invalid Place name");
            }

            List<TouristCompany> companies = touristCompanyRepository.findAllByTouristPlacesName(criteriaValue);
            result.addAll(companies);

            // Sort places in descending order of tariff details
            // excluder tourist places not matching criteriaValue
            result = result.stream().peek(touristCompany -> touristCompany.setTouristPlaces(touristCompany.getTouristPlaces().stream()
                    .filter(touristPlace -> touristPlace.getName().equalsIgnoreCase(criteriaValue)).toList())).toList();
            return result.stream().sorted((o1, o2) -> {
                if (o1.getTouristPlaces().get(0).getTariff() > o2.getTouristPlaces().get(0).getTariff()) {
                    return -1;
                } else if (o1.getTouristPlaces().get(0).getTariff() < o2.getTouristPlaces().get(0).getTariff()) {
                    return 1;
                } else {
                    return 0;
                }
            }).toList();
        } else {
            throw new IllegalArgumentException("Invalid search criteria");
        }

        return result;
    }

}
