package com.rest.domain.dto;

import java.time.LocalDate;

import com.rest.domain.entity.GoalEntity;
import com.rest.domain.entity.UserEntity;

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
public class GoalUpdateDto {
	
	private LocalDate startDate;
	private LocalDate endDate;
	
	@Default
	private double goalSoju = -1;

	@Default
	private double goalBeer = -1;
	@Default
	private String title ="default";

	
	public GoalEntity toEntity(UserEntity user) {
		return GoalEntity.builder().startDate(startDate).endDate(endDate).goalBeer(goalBeer)
				.goalSoju(goalSoju).title(title).user(user).build();
	}
}
