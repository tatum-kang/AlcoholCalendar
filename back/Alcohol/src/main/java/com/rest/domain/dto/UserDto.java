package com.rest.domain.dto;

import java.util.Collections;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.rest.domain.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
@Setter
public class UserDto {
	
	@Email(message = "이메일 형식에 맞지 않습니다.")
	private String email;
	
	private String password;
	
	private String name;
	
	private String nickname;
	
	public UserEntity toEntity(String password, String authkey) {
		return UserEntity.builder()
				.email(email)
				.password(password)
				.emailAuthBool(false)
				.nickname(nickname)
				.name(name)
				.emailAuthKey(authkey)
				.roles(Collections.singletonList("ROLE_USER"))
				.build();
	}

}
