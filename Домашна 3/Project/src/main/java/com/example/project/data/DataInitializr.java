package com.example.project1.data;

import com.example.project1.data.pipeline.Pipe;
import com.example.project1.data.pipeline.impl.CompanyExtractionFilter;
import com.example.project1.data.pipeline.impl.DataAvailabilityCheckFilter;
import com.example.project1.data.pipeline.impl.DataCompletionFilter;
import com.example.project1.entity.CompanyEntity;
import com.example.project1.repository.CompanyRepository;
import com.example.project1.repository.HistoricalDataRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializr {

    private final CompanyRepository companyRepository;
    private final HistoricalDataRepository historicalDataRepository;

    @PostConstruct
    private void initializeData() throws IOException, ParseException {
        long startTime = System.nanoTime();

        Pipe<List<CompanyEntity>> pipe = new Pipe<>();
        pipe.addFilter(new CompanyExtractionFilter(companyRepository));
        pipe.addFilter(new DataAvailabilityCheckFilter(companyRepository, historicalDataRepository));
        pipe.addFilter(new DataCompletionFilter(companyRepository, historicalDataRepository));
        pipe.runFilter(null);

        long endTime = System.nanoTime();
        long durationInMillis = (endTime - startTime) / 1_000_000;

        long hours = durationInMillis / 3_600_000;
        long minutes = (durationInMillis % 3_600_000) / 60_000;
        long seconds = (durationInMillis % 60_000) / 1_000;

        System.out.printf("Total time for all filters to complete: %02d hours, %02d minutes, %02d seconds%n", hours, minutes, seconds);
    }

}
