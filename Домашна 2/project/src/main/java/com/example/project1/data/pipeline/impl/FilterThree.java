package com.example.project1.data.pipeline.impl;

import com.example.project1.data.DataTransformer;
import com.example.project1.data.pipeline.Filter;
import com.example.project1.entity.CompanyProfileEntity;
import com.example.project1.entity.AssetHistoryEntity;
import com.example.project1.repository.CompanyProfileRepository;
import com.example.project1.repository.AssetHistoryRepository;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class FilterThree implements Filter<List<CompanyProfileEntity>> {

    private final CompanyProfileRepository companyProfileRepository;
    private final AssetHistoryRepository assetHistoryRepository;

    private static final String HISTORICAL_DATA_URL = "https://www.mse.mk/mk/stats/symbolhistory/";

    public FilterThree(CompanyProfileRepository companyProfileRepository, AssetHistoryRepository assetHistoryRepository) {
        this.companyProfileRepository = companyProfileRepository;
        this.assetHistoryRepository = assetHistoryRepository;
    }

    public List<CompanyProfileEntity> execute(List<CompanyProfileEntity> input) throws IOException, ParseException {

        for (CompanyProfileEntity company : input) {
            LocalDate fromDate = LocalDate.now();
            LocalDate toDate = LocalDate.now().plusYears(1);
            addHistoricalData(company, fromDate, toDate);
        }

        return null;
    }

    private void addHistoricalData(CompanyProfileEntity company, LocalDate fromDate, LocalDate toDate) throws IOException {
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
                    LocalDate date = DataTransformer.parseDate(columns.get(0).text(), "d.M.yyyy");

                    if (assetHistoryRepository.findByDateAndCompany(date, company).isEmpty()) {

                        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);

                        Double lastTransactionPrice = DataTransformer.parseDouble(columns.get(1).text(), format);
                        Double maxPrice = DataTransformer.parseDouble(columns.get(2).text(), format);
                        Double minPrice = DataTransformer.parseDouble(columns.get(3).text(), format);
                        Double averagePrice = DataTransformer.parseDouble(columns.get(4).text(), format);
                        Double percentageChange = DataTransformer.parseDouble(columns.get(5).text(), format);
                        Integer quantity = DataTransformer.parseInteger(columns.get(6).text(), format);
                        Integer turnoverBest = DataTransformer.parseInteger(columns.get(7).text(), format);
                        Integer totalTurnover = DataTransformer.parseInteger(columns.get(8).text(), format);

                        if (maxPrice != null) {

                            if (company.getLastUpdated() == null || company.getLastUpdated().isBefore(date)) {
                                company.setLastUpdated(date);
                            }

                            AssetHistoryEntity assetHistoryEntity = new AssetHistoryEntity(
                                    date, lastTransactionPrice, maxPrice, minPrice, averagePrice, percentageChange,
                                    quantity, turnoverBest, totalTurnover);
                            assetHistoryEntity.setCompany(company);
                            assetHistoryRepository.save(assetHistoryEntity);
                            company.getHistoricalData().add(assetHistoryEntity);
                        }
                    }
                }
            }
        }
        companyProfileRepository.save(company);
    }


}
