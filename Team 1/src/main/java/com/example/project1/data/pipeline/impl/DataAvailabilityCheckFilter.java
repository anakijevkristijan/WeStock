package com.example.project1.data.pipeline.impl;

import com.example.project1.data.DataParser;
import com.example.project1.data.pipeline.Filter;
import com.example.project1.entity.CompanyEntity;
import com.example.project1.entity.HistoricalDataEntity;
import com.example.project1.repository.CompanyRepository;
import com.example.project1.repository.HistoricalDataRepository;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataAvailabilityCheckFilter implements Filter<List<CompanyEntity>> {

    private final CompanyRepository companyRepository;
    private final HistoricalDataRepository historicalDataRepository;

    private static final String HISTORICAL_DATA_URL = "https://www.mse.mk/mk/stats/symbolhistory/";

    public DataAvailabilityCheckFilter(CompanyRepository companyRepository, HistoricalDataRepository historicalDataRepository) {
        this.companyRepository = companyRepository;
        this.historicalDataRepository = historicalDataRepository;
    }

    public List<CompanyEntity> execute(List<CompanyEntity> input) throws IOException, ParseException {
        List<CompanyEntity> companies = new ArrayList<>();

        for (CompanyEntity company : input) {
            if (company.getLastUpdated() == null) {
                for (int i = 1; i <= 10; i++) {
                    int temp = i - 1;
                    LocalDate fromDate = LocalDate.now().minusYears(i);
                    LocalDate toDate = LocalDate.now().minusYears(temp);
                    addHistoricalData(company, fromDate, toDate);
                }
            } else {
                companies.add(company);
            }
        }

        return companies;
    }

    private void addHistoricalData(CompanyEntity company, LocalDate fromDate, LocalDate toDate) throws IOException {
        Connection.Response response = Jsoup.connect(HISTORICAL_DATA_URL + company.getCompanyCode())
                .data("FromDate", fromDate.toString())
                .data("ToDate", toDate.toString())
                .method(Connection.Method.POST)
                .execute();

        Document document = response.parse();

        Element table = document.select("table#resultsTable").first();

        if (table != null) {
            Elements rows = table.select("tbody tr");

            for (Element row : rows) {
                Elements columns = row.select("td");

                if (columns.size() > 0) {
                    LocalDate date = DataParser.parseDate(columns.get(0).text(), "d.M.yyyy");

                    if (date != null && historicalDataRepository.findByDateAndCompany(date, company).isEmpty()) {
                        if (company.getLastUpdated() == null || company.getLastUpdated().isBefore(date)) {
                            company.setLastUpdated(date);
                            companyRepository.save(company);
                        }

                        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);

                        Double lastTransactionPrice = DataParser.parseDouble(columns.get(1).text(), format);
                        Double maxPrice = DataParser.parseDouble(columns.get(2).text(), format);
                        Double minPrice = DataParser.parseDouble(columns.get(3).text(), format);
                        Double averagePrice = DataParser.parseDouble(columns.get(4).text(), format);
                        Double percentageChange = DataParser.parseDouble(columns.get(5).text(), format);
                        Integer quantity = DataParser.parseInteger(columns.get(6).text(), format);
                        Integer turnoverBest = DataParser.parseInteger(columns.get(7).text(), format);
                        Integer totalTurnover = DataParser.parseInteger(columns.get(8).text(), format);

                        HistoricalDataEntity historicalDataEntity = new HistoricalDataEntity(
                                date, lastTransactionPrice, maxPrice, minPrice, averagePrice, percentageChange,
                                quantity, turnoverBest, totalTurnover);
                        historicalDataEntity.setCompany(company);

                        historicalDataRepository.save(historicalDataEntity);
                    }
                }
            }
        }
    }

}
