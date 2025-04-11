package com.amnex.rorsync.dto.response;

import lombok.Data;

@Data
public class UserLoginResponse {
    private String userName;
    private String userToken;
}
