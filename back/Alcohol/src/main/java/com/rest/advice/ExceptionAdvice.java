package com.rest.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rest.advice.exception.CEmailConfirmFailedException;
import com.rest.advice.exception.CEmailSigninFailedException;
import com.rest.advice.exception.CUserNotCorrectException;
import com.rest.advice.exception.CUserNotFoundException;
import com.rest.domain.response.CommonResult;
import com.rest.service.ResponseService;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {
	private final ResponseService responseService;

//	@ExceptionHandler(Exception.class)
//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//	protected CommonResult defaultException(HttpServletRequest request, Exception e) {
//		return responseService.getFailResult();
//	}

	private final MessageSource messageSource;

	@ExceptionHandler(CEmailSigninFailedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult emailSigninFailed(HttpServletRequest request, CEmailSigninFailedException e) {
		return responseService.getFailResult(Integer.valueOf(getMessage("emailSigninFailed.code")),
				getMessage("emailSigninFailed.msg"));
	}
	
	@ExceptionHandler(CUserNotCorrectException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult userNotCorrect(HttpServletRequest request, CUserNotCorrectException e) {
		return responseService.getFailResult(Integer.valueOf(getMessage("userNotCorrected.code")),
				getMessage("userNotCorrect.msg"));
	}
	
	@ExceptionHandler(CEmailConfirmFailedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult emailConfirmFailed(HttpServletRequest request, CEmailConfirmFailedException e) {
		return responseService.getFailResult(Integer.valueOf(getMessage("emailConfirmFailed.code")),
				getMessage("emailConfirmFailed.msg"));
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult defaultException(HttpServletRequest request, Exception e) {
		// 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
		return responseService.getFailResult(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.msg"));
	}

	@ExceptionHandler(CUserNotFoundException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
		// 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
		return responseService.getFailResult(Integer.valueOf(getMessage("userNotFound.code")),
				getMessage("userNotFound.msg"));
	}

	// code정보에 해당하는 메시지를 조회합니다.
	private String getMessage(String code) {
		return getMessage(code, null);
	}

	// code정보, 추가 argument로 현재 locale에 맞는 메시지를 조회합니다.
	private String getMessage(String code, Object[] args) {
		return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
	}

}
