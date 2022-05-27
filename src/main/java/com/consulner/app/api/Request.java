package com.consulner.app.api;

import java.util.List;
import java.util.Map;

public abstract class Request {
    private Map<String, List<String>> queryParams;

    protected Request(Map<String, List<String>> queryParams) {
        this.queryParams = queryParams;
    }

    public Map<String, List<String>> getQueryParams(){
        return queryParams;
    }
}
