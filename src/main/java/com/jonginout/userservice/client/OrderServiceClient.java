package com.jonginout.userservice.client;

import com.jonginout.userservice.vo.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

// name 에는 MSA NAME
@FeignClient(name = "order-service")
public interface OrderServiceClient {

    @GetMapping("/order-service/{userId}/orders")
    ResponseEntity<List<ResponseOrder>> getOrders(
            @PathVariable String userId
    );
}
