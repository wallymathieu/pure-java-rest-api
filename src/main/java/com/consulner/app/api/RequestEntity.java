package com.consulner.app.api;

import java.util.List;
import java.util.Map;

public class RequestEntity<RequestBody> extends Request {
    private RequestBody requestBody;

    public RequestEntity(RequestBody requestBody, Map<String, List<String>> queryParams) {
        super(queryParams);
        this.requestBody = requestBody;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }
}
