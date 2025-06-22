package com.airbnb.client;

import com.airbnb.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://localhost:8081")
public interface NotificationClient {
    @PostMapping("/api/notify/send")
    void sendNotification(@RequestBody NotificationRequest request);
}

