package com.airbnb.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.airbnb.dto.NotificationRequest;

@FeignClient(name = "notification-service", url = "http://localhost:8081")
public interface NotificationClient {
    @PostMapping("/api/notify/send")
    void sendNotification(@RequestBody NotificationRequest request);
}

