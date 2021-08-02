package com.jonginout.userservice.dto;

import com.jonginout.userservice.vo.ResponseOrder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private Date createdAt;
    private String encPwd;

    private List<ResponseOrder> orders;
}
