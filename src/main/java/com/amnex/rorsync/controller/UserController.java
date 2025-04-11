package com.amnex.rorsync.controller;

import com.amnex.rorsync.dto.request.UserDto;
import com.amnex.rorsync.dto.response.ResponseModel;
import com.amnex.rorsync.dto.response.UserLoginResponse;
import com.amnex.rorsync.service.UserService;
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
