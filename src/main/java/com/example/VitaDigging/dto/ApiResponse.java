package com.example.VitaDigging.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApiResponse {

    @JsonProperty("I0030")
    private I0030 i0030;

    @Getter
    @Setter
    public static class I0030 {
        private List<ProductDto> row;
    }
}