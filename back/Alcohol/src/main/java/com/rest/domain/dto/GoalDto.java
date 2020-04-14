package com.rest.domain.dto;

import java.time.LocalDate;

import com.rest.domain.entity.GoalEntity;
import com.rest.domain.entity.UserEntity;

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
public class GoalDto {
	
	private LocalDate startDate;
	private LocalDate endDate;
	private double goalSoju;
	private double goalBeer;
	private String title;

	
	public GoalEntity toEntity(UserEntity user) {
		return GoalEntity.builder().startDate(startDate).endDate(endDate).goalBeer(goalBeer)
				.goalSoju(goalSoju).title(title).user(user).build();
	}
}
