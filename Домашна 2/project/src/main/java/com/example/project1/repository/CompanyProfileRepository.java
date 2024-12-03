package com.example.project1.repository;

import com.example.project1.entity.CompanyProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyProfileRepository extends JpaRepository<CompanyProfileEntity, Long> {
    Optional<CompanyProfileEntity> findByCompanyCode(String companyCode);
}
