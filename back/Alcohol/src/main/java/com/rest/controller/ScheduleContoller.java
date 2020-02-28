package com.rest.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rest.advice.exception.CUserNotCorrectException;
import com.rest.config.jwtAuth.JwtTokenProvider;
import com.rest.domain.entity.ScheduleEntity;
import com.rest.domain.entity.UserEntity;
import com.rest.domain.repository.ScheduleRepository;
import com.rest.domain.repository.UserRepository;
import com.rest.domain.response.CommonResult;
import com.rest.domain.response.ScheduleResult;
import com.rest.domain.response.SingleResult;
import com.rest.service.AuthToUserService;
import com.rest.service.ResponseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@RestController
@Api(tags = {"2. Schedule"})
@RequiredArgsConstructor
@RequestMapping("/v1/schedule")
@Transactional
public class ScheduleContoller {
	private final ScheduleRepository scheduleRepository;
	private final ResponseService responseService;
	private final UserRepository userRepository;
	private final AuthToUserService authToUserService;
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "일정 등록", notes = "일정 등록하기")
	@PostMapping("/save")
	public SingleResult<ScheduleEntity> save(
				@ApiParam(value = "일정 날짜 시간", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam LocalDateTime goalDateTime,
				@ApiParam(value = "일정 제목", required = true) @RequestParam String title,
				@ApiParam(value = "일정 내용") @RequestParam(defaultValue = "") String content,
				@ApiParam(value = "소주 수량") @RequestParam(defaultValue = "0") double soju,
				@ApiParam(value = "맥주 수량") @RequestParam(defaultValue = "0") double beer){
		UserEntity user = authToUserService.getUser();
		LocalDateTime now = LocalDateTime.now();
		boolean checkScheduel = goalDateTime.isBefore(now) ? true : false;
		ScheduleEntity schedule = ScheduleEntity.builder()
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
		return responseService.getSingleResult(scheduleRepository.save(schedule));
	}
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "일정 조회", notes = "원하는 연, 월 일정 조회하기")
	@GetMapping("/select")
	public ScheduleResult<ScheduleEntity> select(
			@ApiParam(value = "년") @RequestParam int year,
			@ApiParam(value = "월") @RequestParam int month){
		UserEntity user = authToUserService.getUser();
		List<ScheduleEntity> sc = scheduleRepository.findByUserAndYearAndMonth(user, year, month);
		return responseService.getScheduleResult(sc, user);
	}
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "일정 수정", notes = "일정 수정 하기")
	@PutMapping("/update")
	public SingleResult<ScheduleEntity> update(
			@ApiParam(value = "일정 날짜 시간", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam LocalDateTime goalDateTime,
			@ApiParam(value = "일정 제목", required = true) @RequestParam String title,
			@ApiParam(value = "일정 내용", required = true) @RequestParam String content,
			@ApiParam(value = "소주 수량", required = true) @RequestParam double soju,
			@ApiParam(value = "맥주 수량", required = true) @RequestParam double beer,
			@ApiParam(value = "일정 번호", required = true) @RequestParam long sid){
		UserEntity user = authToUserService.getUser();
		if(user != scheduleRepository.getOne(sid).getUser()) {
			throw new CUserNotCorrectException();
		}
		LocalDateTime now = LocalDateTime.now();
		boolean checkScheduel = goalDateTime.isBefore(now) ? true : false;
		ScheduleEntity schedule = ScheduleEntity.builder()
				.sid(sid)
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
		return responseService.getSingleResult(scheduleRepository.save(schedule));
	}
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "일정 삭제", notes = "일정 삭제 하기")
	@DeleteMapping("/delete")
	public CommonResult delete(
			@ApiParam(value = "일정 번호", required = true) @RequestParam long sid) {
		UserEntity user = authToUserService.getUser();
		if(user != scheduleRepository.getOne(sid).getUser()) {
			throw new CUserNotCorrectException();
		}
		
		scheduleRepository.deleteById(sid);
		return responseService.getSuccessResult();
	}
}
