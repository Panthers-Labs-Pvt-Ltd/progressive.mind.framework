package com.progressive.minds.chimera.dataquality.service;

import com.progressive.minds.chimera.dataquality.common.DataProduct;
import com.progressive.minds.chimera.dataquality.entities.DQRules;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataProductService {

    private DataProduct dataProduct;
    private Map<String, List<DQRules>> columnRules = new HashMap<>();
    private List<DQRules> productRules;

    public DataProductService(DataProduct dataProduct) {
        this.dataProduct = dataProduct;
    }

    // Sets data quality rules for the entire data product
    public void setProductRules(List<DQRules> rules) {
        this.productRules = rules;
    }

    // Gets data quality rules for the entire data product
    public List<DQRules> getProductRules() {
        return productRules;
    }

    // Sets data quality rules for a specific column
    public void setColumnRules(String columnName, List<DQRules> rules) {
        columnRules.put(columnName, rules);
    }

    // Gets data quality rules for a specific column
    public List<DQRules> getColumnRules(String columnName) {
        return columnRules.get(columnName);
    }
}