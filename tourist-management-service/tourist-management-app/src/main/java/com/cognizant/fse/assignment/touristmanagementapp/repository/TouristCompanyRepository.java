package com.cognizant.fse.assignment.touristmanagementapp.repository;
import com.cognizant.fse.assignment.touristmanagementapp.domain.TouristCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TouristCompanyRepository extends JpaRepository<TouristCompany, Long> {
}