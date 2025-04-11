package com.amnex.rorsync.service.impl;

import com.amnex.rorsync.configuration.jwt.JwtTokenUtil;
import com.amnex.rorsync.configuration.jwt.JwtUserDetailsService;
import com.amnex.rorsync.dto.request.UserDto;
import com.amnex.rorsync.dto.response.ResponseModel;
import com.amnex.rorsync.dto.response.UserLoginResponse;
import com.amnex.rorsync.entity.UserMaster;
import com.amnex.rorsync.repository.UserMasterRepository;
import com.amnex.rorsync.service.UserService;
import com.amnex.rorsync.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Override
    public ResponseModel login(UserDto userDto) {
        ResponseModel model = new ResponseModel();
        try {
            Optional<UserMaster> userMasterOp =  userMasterRepository.findByUserNameAndIsDeletedAndIsActive(userDto.getUserName());
            if(userMasterOp.isPresent()) {
                UserMaster userMaster = userMasterOp.get();
                String token = null;
                if (passwordEncoder.matches(userDto.getPassword(), userMaster.getUserPassword())) {
                    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername((userMaster.getUserId() + ""));
                    token = jwtTokenUtil.generateToken(userDetails);
                } else {
                    model.setCode(Constants.INTERNAL_ERROR_CODE);
                    model.setMessage(Constants.INVALID_PASSWORD);
                }
                if(token != null) {
                    userMaster.setUserToken(token);
                    userMasterRepository.save(userMaster);

                    UserLoginResponse loginResponse = new UserLoginResponse();
                    loginResponse.setUserToken(token);
                    loginResponse.setUserName(userMaster.getUserName());

                    model.setData(loginResponse);
                    model.setCode(Constants.SUCCESS_CODE);
                    model.setMessage(Constants.SUCCESS);
                } else {
                    model.setCode(Constants.INTERNAL_ERROR_CODE);
                    model.setMessage(Constants.SOMETHING_WENT_WRONG);
                }
            } else {
                model.setCode(Constants.NOT_FOUND_CODE);
                model.setMessage(Constants.No_DATA_FOUND);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return model;
    }
}
