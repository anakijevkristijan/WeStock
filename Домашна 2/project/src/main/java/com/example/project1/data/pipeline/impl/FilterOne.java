package com.example.project1.data.pipeline.impl;

import com.example.project1.data.pipeline.Filter;
import com.example.project1.entity.CompanyProfileEntity;
import com.example.project1.repository.CompanyProfileRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class FilterOne implements Filter<List<CompanyProfileEntity>> {

    private final CompanyProfileRepository companyProfileRepository;

    public FilterOne(CompanyProfileRepository companyProfileRepository) {
        this.companyProfileRepository = companyProfileRepository;
    }

    private static final String STOCK_MARKET_URL = "https://www.mse.mk/mk/stats/symbolhistory/kmb";

    @Override
    public List<CompanyProfileEntity> execute(List<CompanyProfileEntity> input) throws IOException {
        Document document = Jsoup.connect(STOCK_MARKET_URL).get();
        Element selectMenu = document.select("select#Code").first();

        if (selectMenu != null) {
            Elements options = selectMenu.select("option");
            for (Element option : options) {
                String code = option.attr("value");
                if (!code.isEmpty() && code.matches("^[a-zA-Z]+$")) {
                    if (companyProfileRepository.findByCompanyCode(code).isEmpty()) {
                        companyProfileRepository.save(new CompanyProfileEntity(code));
                    }
                }
            }
        }

        return companyProfileRepository.findAll();
    }
}
