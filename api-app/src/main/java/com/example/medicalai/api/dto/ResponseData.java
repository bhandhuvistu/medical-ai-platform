package com.example.medicalai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData {
    private String message;
    private int statusCode;
    private String status;
    private Object data;

    public ResponseData(String message, int statusCode, String status) {
        this.message = message;
        this.statusCode = statusCode;
        this.status = status;
    }
}
