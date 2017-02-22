package com.jcs.sbs.common;

import java.util.List;
import java.util.Map;

public class CommonResponse {

    private Map<String, String> tableHeaders;
    private List tableData;
    private List<String> keys;
    private int totalResults;

    public Map<String, String> getTableHeaders() {
        return tableHeaders;
    }

    public void setTableHeaders(Map<String, String> tableHeaders) {
        this.tableHeaders = tableHeaders;
    }

    public List getTableData() {
        return tableData;
    }

    public void setTableData(List tableData) {
        this.tableData = tableData;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

}
