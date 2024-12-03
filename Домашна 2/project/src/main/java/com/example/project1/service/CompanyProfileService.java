package com.example.project1.service;

import com.example.project1.entity.CompanyProfileEntity;
import com.example.project1.repository.CompanyProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyProfileService {

    private final CompanyProfileRepository companyProfileRepository;

    public List<CompanyProfileEntity> findAll() {
        return companyProfileRepository.findAll();
    }

    public CompanyProfileEntity findById(Long id) throws Exception {
        return companyProfileRepository.findById(id).orElseThrow(Exception::new);
    }

}
