package com.engsoft.linkederasmus.service;

import com.engsoft.linkederasmus.dto.UserDto;
import com.engsoft.linkederasmus.entity.User;

import java.util.List;

public interface UserService {

    void saveUser(UserDto userDto);

    User findUserByUsername(String name);

    List<UserDto> findAllUsers();
}