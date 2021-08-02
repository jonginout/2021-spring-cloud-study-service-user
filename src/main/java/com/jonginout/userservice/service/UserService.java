package com.jonginout.userservice.service;

import com.jonginout.userservice.dto.UserDto;
import com.jonginout.userservice.jpa.UserEntity;
import javassist.NotFoundException;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId) throws NotFoundException;

    Iterable<UserEntity> getUserByAll();
}
