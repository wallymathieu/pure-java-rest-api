package com.consulner.app.api;

import java.util.List;
import java.util.Map;

public class EmptyRequest extends Request {
    protected EmptyRequest(Map<String, List<String>> queryParams) {
        super(queryParams);
    }
}
