package com.dev.ecommerce.emailservice.clients;

import com.dev.ecommerce.emailservice.EmailUtil;
import com.dev.ecommerce.emailservice.dtos.EmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Component
public class KafkaConsumerClient {
    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics="Signup", groupId = "emailService")
    public void sendEmail(String message) {
        try {
            System.out.println("Message received from Kafka topic: " + message);
            EmailDto emailDto = objectMapper.readValue(message, EmailDto.class);

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailDto.getFrom(), "jahg dcgb qsqk ysas");
                }
            };
            Session session = Session.getInstance(props, auth);

            EmailUtil.sendEmail(session, emailDto.getTo(), emailDto.getSubject(), emailDto.getBody());

        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @KafkaListener(topics="OrderCreated", groupId = "emailService")
    public void sendOrderEmail(String message) {
        try {
            System.out.println("Message received from Kafka topic OrderCreated: " + message);
            com.dev.ecommerce.emailservice.dtos.OrderDto orderDto = objectMapper.readValue(message, com.dev.ecommerce.emailservice.dtos.OrderDto.class);

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("abinashsethi97@gmail.com", "jahg dcgb qsqk ysas");
                }
            };
            Session session = Session.getInstance(props, auth);

            String toAddress = orderDto.getCustomerEmail();
            if (toAddress == null || toAddress.isBlank()) {
                throw new IllegalArgumentException("Order email event does not include customerEmail");
            }

            String subject = "Order Confirmed - #" + orderDto.getId();
            String body = "Hi,\n\nYour order #" + orderDto.getId() + " has been successfully created.\nTotal Amount: " + orderDto.getTotalAmount() + "\nStatus: " + orderDto.getStatus() + "\n\nThank you for shopping with us!";

            EmailUtil.sendEmail(session, toAddress, subject, body);

        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }
}
