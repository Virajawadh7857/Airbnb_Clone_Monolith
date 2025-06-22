package com.notification.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String toEmail;
    private String subject;
    private String body;

    private String toPhoneNumber;
    private String smsMessage;
}

