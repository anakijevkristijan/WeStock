package com.example.project1.data;

import com.example.project1.repository.AssetHistoryRepository;
import com.example.project1.repository.CompanyProfileRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final CompanyProfileRepository companyProfileRepository;
    private final AssetHistoryRepository assetHistoryRepository;

    @PostConstruct
    private void initializeData() throws IOException, ParseException {
//        long startTime = System.nanoTime();
//
//        Pipe<List<CompanyProfileEntity>> pipe = new Pipe<>();
//        pipe.addFilter(new FilterOne(companyRepository));
//        pipe.addFilter(new FilterTwo(companyRepository, historicalDataRepository));
//        pipe.addFilter(new FilterThree(companyRepository, historicalDataRepository));
//        pipe.runFilter(null);
//
//        long endTime = System.nanoTime();
//        long durationInMillis = (endTime - startTime) / 1_000_000;
//
//        long hours = durationInMillis / 3_600_000;
//        long minutes = (durationInMillis % 3_600_000) / 60_000;
//        long seconds = (durationInMillis % 60_000) / 1_000;
//
//        System.out.printf("Total time for all filters to complete: %02d hours, %02d minutes, %02d seconds%n", hours, minutes, seconds);
    }

}
