package com.amnex.fr_farmer_farm_sync.controller;

import com.amnex.fr_farmer_farm_sync.dto.request.UserDto;
import com.amnex.fr_farmer_farm_sync.dto.response.ResponseModel;
import com.amnex.fr_farmer_farm_sync.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public ResponseModel login(@RequestBody @Valid UserDto userDto) {
        logger.info("Request for Login with User:"+userDto.getUserName());
        return userService.login(userDto);
    }
}
