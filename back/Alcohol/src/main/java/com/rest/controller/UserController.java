package com.rest.controller;

import javax.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rest.advice.exception.CEmailConfirmFailedException;
import com.rest.advice.exception.CEmailSigninFailedException;
import com.rest.advice.exception.CUserNotCorrectException;
import com.rest.config.emailAuth.TempKey;
import com.rest.config.jwtAuth.JwtTokenProvider;
import com.rest.domain.dto.UserDto;
import com.rest.domain.dto.UserLoginDto;
import com.rest.domain.dto.UserUpdateDto;
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
			@RequestBody UserDto userDto
			) throws Exception{
		
		String authkey = new TempKey().getKey(50, false);
		UserEntity user = userDto.toEntity(passwordEncoder.encode(userDto.getPassword()), authkey);
		userRepository.save(user);
		user = userRepository.findByEmail(user.getEmail());
		emailAuthService.sendHtmlMail(user);
		return responseService.getSingleResult("email 인증을 확인하여 주세요.");
	}

	@ApiOperation(value = "로그인", notes = "로그인하기")
	@PostMapping("/login")
	public SingleResult<String> login(
			@RequestBody UserLoginDto userLoginDto){
		UserEntity user = userRepository.findByEmail(userLoginDto.getEmail());
		if(!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
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
	@DeleteMapping(value = "/delete/{uid}")
	public CommonResult delete(@PathVariable("uid") long uid) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		UserEntity user = userRepository.findByEmail(email);
		if(user.getUid() != uid) {
			throw new CUserNotCorrectException();
		}
		userRepository.deleteById(uid);
		return responseService.getSuccessResult();
	}
	
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "회원수정", notes = "변경하고자 하는 필드만 보내주세요")
	@PutMapping(value = "/update/{uid}")
	public SingleResult<UserEntity> modify(@RequestBody UserUpdateDto userUpdateDto, @PathVariable("uid") long uid){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		UserEntity user = userRepository.findByEmail(email);
		if(user.getUid() != uid) {
			throw new CUserNotCorrectException();
		}
		user.setPassword(userUpdateDto.getPassword() == "default"? user.getPassword() : passwordEncoder.encode(userUpdateDto.getPassword()));
		user.setName(userUpdateDto.getName() == "default" ? user.getName() : userUpdateDto.getName());
		user.setNickname(userUpdateDto.getNickname() == "default" ? user.getNickname() : userUpdateDto.getNickname());
		return responseService.getSingleResult(userRepository.save(user));
	}
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "회원조회", notes = "회원정보를 조회")
	@GetMapping(value = "/select/{uid}")
	public SingleResult<UserEntity> select(@PathVariable("uid") long uid){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		UserEntity user = userRepository.findByEmail(email);
		if(user.getUid() != uid) {
			throw new CUserNotCorrectException();
		}
		return responseService.getSingleResult(user);
	}
	
	
	@GetMapping("/checkemail")
	@ApiOperation(value = "이메일 중복조회", notes = "true: 중복, false: 중복x")
	public SingleResult<Boolean> checkemail(
			@ApiParam(value = "회원이메일", required = true) @RequestParam String email 
			){
		UserEntity user = userRepository.findByEmail(email);
		if(user != null) {
			return responseService.getSingleResult(true);
		}
		return responseService.getSingleResult(false);
	}
	
	@GetMapping("/checknickname")
	@ApiOperation(value = "닉네임 중복조회",  notes = "true: 중복, false: 중복x")
	public SingleResult<Boolean> checknickname(
			@ApiParam(value = "회원 닉네임", required = true) @RequestParam String nickname 
			){
		UserEntity user = userRepository.findByNickname(nickname);
		if(user != null) {
			return responseService.getSingleResult(true);
		}
		return responseService.getSingleResult(false);
	}
	
}
