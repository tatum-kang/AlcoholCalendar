package com.rest.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rest.domain.entity.UserEntity;
import com.rest.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthToUserService {
	private final UserRepository userRepository;
	public UserEntity getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		UserEntity user = userRepository.findByEmail(email);
		return user;
	}
}
