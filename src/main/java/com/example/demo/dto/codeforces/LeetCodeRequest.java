package com.example.demo.dto.codeforces;

import java.util.Map;

public class LeetCodeRequest {
    private String query;
    private Map<String, Object> variables;
    private String operationName;

    public LeetCodeRequest(String query, Map<String, Object> variables, String operationName) {
        this.query = query;
        this.variables = variables;
        this.operationName = operationName;
    }

    public String getQuery() { return query; }
    public Map<String, Object> getVariables() { return variables; }
    public String getOperationName() { return operationName; }
}