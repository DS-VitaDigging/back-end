package com.example.VitaDigging.dto;

import com.example.VitaDigging.entity.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductDto {
    @JsonProperty("PRDLST_NM")
    private String name;

    private String category;

    @JsonProperty("PRIMARY_FNCLTY")
    private String efficacy;

    @JsonProperty("RAWMTRL_NM")
    private String ingredients;

    @JsonProperty("NTK_MTHD")
    private String instructions;

    @JsonProperty("IFTKN_ATNT_MATR_CN")
    private String precautions;

    @JsonProperty("BSSH_NM")
    private String manufacturer;

    private String purchaseLink;
    private String imageUrl;
}