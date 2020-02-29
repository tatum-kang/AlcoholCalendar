package com.rest.domain.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
@Setter
public class UserLoginDto {
	
	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Email(message = "이메일 형식에 맞지 않습니다.")
	private String email;
	
	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	private String password;
}
