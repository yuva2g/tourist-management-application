package com.cognizant.fse.assignment.touristmanagementqueryapp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TouristPlace {
    private Long id;
    private String name;
    private double tariff;
}
