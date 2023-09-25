package com.runapp.guildservice.feignClient;

import com.runapp.guildservice.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service")
public interface ProfileServiceClient {

    @GetMapping("/users/{userId}")
    ResponseEntity<UserResponse> getUserById(@PathVariable int userId);

}
