package com.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rest.domain.entity.UserEntity;
import com.rest.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class SignupConfirmController {
	private final UserRepository userRepository;
	
	@GetMapping("/signup/confirm")
	public String emailConfirm(@RequestParam String authkey, @RequestParam Long uid) {
		UserEntity user = userRepository.getOne(uid);
		if(user.getEmailAuthKey().equals(authkey)) {
			user.setEmailAuthBool(true);
			userRepository.save(user);
		}
		return "EmailConfirm.html";
	}
}
