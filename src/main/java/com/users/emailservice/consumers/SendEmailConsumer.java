package com.users.emailservice.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SendEmailConsumer {
    @KafkaListener(topics = "sendEmail", groupId = "email-service")
    public void sendEmail(String message)
    {
        System.out.println(message);
    }
}
