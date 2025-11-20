package com.sparta.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateRepoResponse {

    private Data data;

    private List<ErrorsItem> errors;

    public Data getData() {
        return data;
    }

    public List<ErrorsItem> getErrors() {
        return errors;
    }
}
