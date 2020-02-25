package com.rest.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.rest.domain.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailAuthService {
	private final JavaMailSender javaMailSender;
	private final SpringTemplateEngine templateEngine;
	
	public void sendHtmlMail(UserEntity user) throws MessagingException {
		MimeMessage mail = javaMailSender.createMimeMessage();
		Context context = new Context();
		context.setVariable("user", user);
		String body = templateEngine.process("Email.html", context);
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(user.getEmail());
		helper.setSubject("[Alcohol Calendar's] 회원가입 이메일 인증");
		helper.setText(body, true);
		javaMailSender.send(mail);
	}
}
