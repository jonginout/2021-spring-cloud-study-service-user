package com.jonginout.userservice.service;

import com.jonginout.userservice.client.OrderServiceClient;
import com.jonginout.userservice.dto.UserDto;
import com.jonginout.userservice.jpa.UserEntity;
import com.jonginout.userservice.jpa.UserRepository;
import com.jonginout.userservice.vo.ResponseOrder;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;
    Environment environment;
    RestTemplate restTemplate;
    OrderServiceClient orderServiceClient;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder,
            Environment environment,
            RestTemplate restTemplate,
            OrderServiceClient orderServiceClient
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
        this.restTemplate = restTemplate;
        this.orderServiceClient = orderServiceClient;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncPwd(passwordEncoder.encode(userDto.getPwd()));
        userRepository.save(userEntity);

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

        return returnUserDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) throws NotFoundException {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new NotFoundException("존재하지 않는 회원입니다.");
        }

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

//        String orderUrl = environment.getProperty("order_service.url") + "/" + userId + "/orders";
//        ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(
//                orderUrl,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<ResponseOrder>>() {
//                }
//        );

//        List<ResponseOrder> orderList = null;
//
//        try {
//            ResponseEntity<List<ResponseOrder>> orderListResponse = orderServiceClient.getOrders(userId);
//            orderList = orderListResponse.getBody();
//        } catch (FeignException e) {
//            log.error(e.getLocalizedMessage());
//        }

        ResponseEntity<List<ResponseOrder>> orderListResponse = orderServiceClient.getOrders(userId);
        List<ResponseOrder> orderList = orderListResponse.getBody();

        userDto.setOrders(orderList);

        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findFirstByEmailOrderByIdDesc(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(userEntity.getEmail(), userEntity.getEncPwd(), true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String username) {
        UserEntity userEntity = userRepository.findFirstByEmailOrderByIdDesc(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        return userDto;
    }
}
