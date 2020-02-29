package com.rest.domain.dto;

import com.rest.domain.dto.UserDto.UserDtoBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
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
public class UserUpdateDto {
	@Default
	private String password = "default";
	@Default
	private String name= "default";
	@Default
	private String nickname = "default";
}
