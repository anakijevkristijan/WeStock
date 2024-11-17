package com.example.project1.repository;

import com.example.project1.entity.CompanyEntity;
import com.example.project1.entity.HistoricalDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HistoricalDataRepository extends JpaRepository<HistoricalDataEntity, Long> {
    Optional<HistoricalDataEntity> findByDateAndCompany(LocalDate date, CompanyEntity company);
}
