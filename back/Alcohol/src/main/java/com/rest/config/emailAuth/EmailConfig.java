package com.rest.config.emailAuth;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.yml")
public class EmailConfig {
    Properties properties = new Properties();

//    @Value("mail.smtp.port")
//    private String port;
//    @Value("mail.smtp.socketFactory.port")
//    private String socketPort;
//    @Value("mail.smtp.auth")
//    private boolean auth;
//    @Value("mail.smtp.starttls.enable")
//    private boolean starttls;
//    @Value("mail.smtp.starttls.required")
//    private boolean startlls_required;
//    @Value("mail.smtp.socketFactory.fallback")
//    private boolean fallback;
//    @Value("${mail.smtp.username}")
//    private String username;
//    @Value("${mail.smtp.password}")
//    private String password;
    

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setUsername("heewkk@gmail.com");
        javaMailSender.setPassword("hoxwbxvcjtqpoodm");
        javaMailSender.setPort(465);
        properties.put("mail.smtp.socketFactory.port", 465);
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.starttls.required", true);
        properties.put("mail.smtp.socketFactory.fallback", false);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailSender.setJavaMailProperties(properties);
        javaMailSender.setDefaultEncoding("UTF-8");

        return javaMailSender;
    }
}
