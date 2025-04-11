package com.amnex.fr_farmer_farm_sync.service;

import com.amnex.fr_farmer_farm_sync.dto.request.UserDto;
import com.amnex.fr_farmer_farm_sync.dto.response.ResponseModel;
import com.amnex.fr_farmer_farm_sync.dto.response.UserLoginResponse;
import org.springframework.stereotype.Service;

public interface UserService {

    public ResponseModel login(UserDto userDto);
}
