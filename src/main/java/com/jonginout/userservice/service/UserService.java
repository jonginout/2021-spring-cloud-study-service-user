package com.jonginout.userservice.service;

import com.jonginout.userservice.dto.UserDto;
import com.jonginout.userservice.jpa.UserEntity;
import javassist.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId) throws NotFoundException;

    Iterable<UserEntity> getUserByAll();

    @Override
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;

    UserDto getUserDetailsByEmail(String username);
}
