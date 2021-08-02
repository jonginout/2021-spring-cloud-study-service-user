package com.jonginout.userservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
// 어떤 필드는 데이터가 없는 경우가 있는게 문제 -> josn 데이터중에 null은 버리고 그렇지 않은것만 응답
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUser {
    private String email;
    private String name;
    private String userId;

    private List<ResponseOrder> orders;
}
