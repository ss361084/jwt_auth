package com.amnex.rorsync.dto.response;

import com.amnex.rorsync.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModel {
    private Object data;
    private Long code;
    private String message;

    ResponseModel(Object data, String message) {
        this.data = data;
        this.message = message;
        this.code = Constants.SUCCESS_CODE;
    }
}
