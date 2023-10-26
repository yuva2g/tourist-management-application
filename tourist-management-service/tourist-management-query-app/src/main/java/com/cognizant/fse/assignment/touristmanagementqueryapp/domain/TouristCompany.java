package com.cognizant.fse.assignment.touristmanagementqueryapp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "touristCompanies")
public class TouristCompany {

    @Id
    private Long id;

    private String branchName;

    private String website;

    private String contact;

    private String email;

    private List<TouristPlace> touristPlaces;

}