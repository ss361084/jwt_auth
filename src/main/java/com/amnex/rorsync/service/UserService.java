package com.amnex.rorsync.service;

import com.amnex.rorsync.dto.request.UserDto;
import com.amnex.rorsync.dto.response.ResponseModel;
import com.amnex.rorsync.dto.response.UserLoginResponse;
import org.springframework.stereotype.Service;

public interface UserService {

    public ResponseModel login(UserDto userDto);
}
