package com.rest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rest.domain.repository.UserRepository;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@RestController
@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {
	private final UserRepository userRepository;

}
