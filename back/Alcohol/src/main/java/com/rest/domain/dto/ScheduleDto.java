package com.rest.domain.dto;

import java.time.LocalDateTime;

import com.rest.domain.entity.ScheduleEntity;
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
public class ScheduleDto {
	private String title;
	private String content;
	
	private double soju;
	private double beer;
	private LocalDateTime goalDateTime;
	
	
	public ScheduleEntity toEntity(UserEntity user) {
		boolean checkScheduel = goalDateTime.isBefore(LocalDateTime.now()) ? true : false;
		
		return ScheduleEntity.builder()
				.beer(beer)
				.goalDateTime(goalDateTime)
				.soju(soju)
				.title(title)
				.content(content)
				.user(user)
				.checkScheduel(checkScheduel)
				.year(goalDateTime.getYear())
				.month(goalDateTime.getMonthValue())
				.day(goalDateTime.getDayOfMonth())
				.build();
	}
	
}
