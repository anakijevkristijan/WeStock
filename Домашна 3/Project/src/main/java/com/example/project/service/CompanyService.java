package com.example.project1.service;

import com.example.project1.entity.CompanyEntity;
import com.example.project1.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<CompanyEntity> findAll() {
        return companyRepository.findAllByOrderByIdAsc();
    }

    public CompanyEntity findById(Long id) throws Exception {
        return companyRepository.findById(id).orElseThrow(Exception::new);
    }

}
