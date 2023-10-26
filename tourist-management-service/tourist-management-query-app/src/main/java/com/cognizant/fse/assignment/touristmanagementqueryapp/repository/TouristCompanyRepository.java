package com.cognizant.fse.assignment.touristmanagementqueryapp.repository;
import com.cognizant.fse.assignment.touristmanagementqueryapp.domain.TouristCompany;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TouristCompanyRepository extends MongoRepository<TouristCompany, Long> {
    List<TouristCompany> findByBranchName(String criteriaValue);

    List<TouristCompany> findAllByTouristPlacesName(String placeName);
}