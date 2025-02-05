package com.progressive.minds.chimera.dataquality.service;

import com.progressive.minds.chimera.dataquality.common.DataAsset;
import com.progressive.minds.chimera.dataquality.entities.DQRules;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAssetService {

    private DataAsset dataAsset;
    private Map<String, List<DQRules>> columnRules = new HashMap<>();
    private List<DQRules> assetRules;

    public DataAssetService(DataAsset dataAsset) {
        this.dataAsset = dataAsset;
    }

    // Sets data quality rules for the entire data asset
    public void setAssetRules(List<DQRules> rules) {
        this.assetRules = rules;
    }

    // Gets data quality rules for the entire data asset
    public List<DQRules> getAssetRules() {
        return assetRules;
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