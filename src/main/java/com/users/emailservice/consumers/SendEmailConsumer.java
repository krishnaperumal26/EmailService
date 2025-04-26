package com.users.emailservice.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.users.emailservice.dtos.SendEmailDto;
import com.users.emailservice.utils.EmailUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

@Service
public class SendEmailConsumer {
    ObjectMapper objectMapper;
    public SendEmailConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @KafkaListener(topics = "sendEmail", groupId = "email-service")
    public void sendEmail(String message)
    {
        SendEmailDto sendEmailDto;
        try {
            sendEmailDto = objectMapper.readValue(message, SendEmailDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(message);

        final String fromEmail = sendEmailDto.getFrom(); //requires valid gmail id
        final String password = "*******"; // add app password for gmail id
        final String toEmail = sendEmailDto.getTo(); // can be any email id

        System.out.println("TLSEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);

        EmailUtil.sendEmail(session, toEmail, sendEmailDto.getSubject(), sendEmailDto.getSubject());

    }
}
