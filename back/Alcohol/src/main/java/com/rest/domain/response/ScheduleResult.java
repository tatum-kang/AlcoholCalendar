package com.rest.domain.response;

import java.util.List;

import com.rest.domain.entity.ScheduleEntity;
import com.rest.domain.entity.UserEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleResult extends CommonResult{
	private List<ScheduleEntity> list;
	private UserEntity user;
}
