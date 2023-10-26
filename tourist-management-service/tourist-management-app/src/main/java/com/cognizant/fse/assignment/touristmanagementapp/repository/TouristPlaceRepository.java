package com.cognizant.fse.assignment.touristmanagementapp.repository;
import com.cognizant.fse.assignment.touristmanagementapp.domain.TouristCompany;
import com.cognizant.fse.assignment.touristmanagementapp.domain.TouristPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TouristPlaceRepository extends JpaRepository<TouristPlace, Long> {
}