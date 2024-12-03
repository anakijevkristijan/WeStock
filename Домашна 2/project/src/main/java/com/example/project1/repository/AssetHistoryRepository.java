package com.example.project1.repository;

import com.example.project1.entity.CompanyProfileEntity;
import com.example.project1.entity.AssetHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssetHistoryRepository extends JpaRepository<AssetHistoryEntity, Long> {
    Optional<AssetHistoryEntity> findByDateAndCompany(LocalDate date, CompanyProfileEntity company);
    List<AssetHistoryEntity> findByCompanyIdAndDateBetween(Long companyId, LocalDate from, LocalDate to);
}
