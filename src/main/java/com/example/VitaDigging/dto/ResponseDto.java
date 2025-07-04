package com.example.VitaDigging.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private Integer status;
    private boolean success;
    private String message;
    private T data;
}