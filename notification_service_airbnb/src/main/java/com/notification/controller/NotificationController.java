package com.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notification.dto.NotificationRequest;
import com.notification.service.EmailService;
import com.notification.service.SmsService;

@RestController
@RequestMapping("/api/notify")
public class NotificationController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
        if (request.getToEmail() != null) {
            emailService.sendEmail(request.getToEmail(), request.getSubject(), request.getBody());
        }

        if (request.getToPhoneNumber() != null) {
            smsService.sendSms(request.getToPhoneNumber(), request.getSmsMessage());
        }

        return ResponseEntity.ok("Notification sent successfully.");
    }
}

