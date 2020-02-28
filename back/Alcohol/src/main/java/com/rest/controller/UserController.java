package com.rest.controller;

import java.util.Collections;

import javax.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rest.advice.exception.CEmailConfirmFailedException;
import com.rest.advice.exception.CEmailSigninFailedException;
import com.rest.advice.exception.CUserNotFoundException;
import com.rest.config.emailAuth.TempKey;
import com.rest.config.jwtAuth.JwtTokenProvider;
import com.rest.domain.entity.UserEntity;
import com.rest.domain.repository.UserRepository;
import com.rest.domain.response.CommonResult;
import com.rest.domain.response.SingleResult;
import com.rest.service.EmailAuthService;
import com.rest.service.ResponseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@RestController
@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@Transactional
@RequestMapping("/v1/user")
public class UserController {
	private final UserRepository userRepository;
	private final ResponseService responseService;
	private final PasswordEncoder passwordEncoder;
	private final EmailAuthService emailAuthService;
	private final JwtTokenProvider jwtTokenProvider;
	
	@PostMapping("/signup")
	@ApiOperation(value = "회원가입", notes = "회원가입하기")
	public SingleResult<String> signup (
			@ApiParam(value ="회원 ID : EMAIL", required = true) @RequestParam String email,
			@ApiParam(value ="회원 비밀번호", required = true) @RequestParam String password,
			@ApiParam(value ="회원 이름", required = true) @RequestParam String name,
			@ApiParam(value ="회원 닉네임", required = true) @RequestParam String nickname) throws Exception{
		
		String authkey = new TempKey().getKey(50, false);
		UserEntity user = UserEntity.builder()
				.email(email)
				.password(passwordEncoder.encode(password))
				.emailAuthBool(false)
				.nickname(nickname)
				.name(name)
				.emailAuthKey(authkey)
				.roles(Collections.singletonList("ROLE_USER"))
				.build();
		userRepository.save(user);
		user = userRepository.findByEmail(user.getEmail());
		emailAuthService.sendHtmlMail(user);
		return responseService.getSingleResult("email 인증을 확인하여 주세요.");
	}

	@ApiOperation(value = "로그인", notes = "로그인하기")
	@PostMapping("/login")
	public SingleResult<String> login(
			@ApiParam(value = "회원 이메일", required = true) @RequestParam String email,
			@ApiParam(value = "회원 비밀번호", required = true) @RequestParam String password){
		UserEntity user = userRepository.findByEmail(email);
		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new CEmailSigninFailedException();
		}
		else {
			if(user.isEmailAuthBool()) {
				return responseService.getSingleResult(jwtTokenProvider.createToken(user.getEmail(), user.getRoles()));
			}
			else {
				throw new CEmailConfirmFailedException();
			}
		}
	}
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "회원삭제", notes="회원정보 삭제")
	@DeleteMapping(value = "/delete")
	public CommonResult delete(@RequestParam String password) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		UserEntity user = userRepository.findByEmail(email);
		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new CEmailSigninFailedException();
		}
		userRepository.deleteById(user.getUid());
		return responseService.getSuccessResult();
	}
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "회원수정", notes = "회원정보를 수정")
	@PutMapping(value = "/update")
	public SingleResult<UserEntity> modify(
			@ApiParam(value = "회원이메일", required = true) @RequestParam String email,
			@ApiParam(value = "회원이름", required = true) @RequestParam String name,
			@ApiParam(value = "회원닉네임", required = true) @RequestParam String nickname
			){
		UserEntity user = userRepository.findByEmail(email);
		user.setName(name);
		user.setNickname(nickname);
		return responseService.getSingleResult(userRepository.save(user));
	}
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "회원조회", notes = "회원정보를 조회")
	@PostMapping(value = "/select")
	public SingleResult<UserEntity> select(
			@ApiParam(value = "회원이메일", required = true) @RequestParam String email
			){
		UserEntity user = userRepository.findByEmail(email);
		return responseService.getSingleResult(user);
	}
}
