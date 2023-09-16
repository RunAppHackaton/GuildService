package com.runapp.guildservice.feignClient;

import com.runapp.guildservice.dto.response.StoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "story-manager-service")
public interface StoryManagementServiceClient {

    @GetMapping(value = "/stories/{storyId}")
    ResponseEntity<StoryResponse> getStoryById(
            @PathVariable int storyId);
}
