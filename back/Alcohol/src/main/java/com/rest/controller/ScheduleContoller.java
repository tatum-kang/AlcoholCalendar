package com.rest.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rest.advice.exception.CUserNotCorrectException;
import com.rest.domain.dto.ScheduleDto;
import com.rest.domain.dto.ScheduleUpdateDto;
import com.rest.domain.entity.ScheduleEntity;
import com.rest.domain.entity.UserEntity;
import com.rest.domain.repository.ScheduleRepository;
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
	private final AuthToUserService authToUserService;
	
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "일정 등록", notes = "날짜양식 :yyyy-MM-dd HH:mm:ss")
	@PostMapping("/save")
	public SingleResult<ScheduleEntity> save(@RequestBody ScheduleDto scheduleDto){
		UserEntity user = authToUserService.getUser();
		ScheduleEntity schedule = scheduleDto.toEntity(user);
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
		for(ScheduleEntity schedule:sc) {
			boolean checkScheduel = schedule.getGoalDateTime().isBefore(LocalDateTime.now()) ? true : false;
			schedule.setCheckScheduel(checkScheduel);
		}
		return responseService.getScheduleResult(sc, user);
	}
	
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "일정 수정", notes = "날짜양식 :yyyy-MM-dd HH:mm:ss")
	@PutMapping("/update/{sid}")
	public SingleResult<ScheduleEntity> update(@PathVariable("sid") long sid,
			@RequestBody ScheduleUpdateDto scheduleUpdateDto){
		
		UserEntity user = authToUserService.getUser();
		ScheduleEntity schedule = scheduleRepository.getOne(sid);
		if(user != schedule.getUser()) {
			throw new CUserNotCorrectException();
		}
		schedule.setBeer(scheduleUpdateDto.getBeer() == -1 ? schedule.getBeer() : scheduleUpdateDto.getBeer());
		schedule.setSoju(scheduleUpdateDto.getSoju() == -1 ? schedule.getSoju() : scheduleUpdateDto.getSoju());
		schedule.setContent(scheduleUpdateDto.getContent() == "default" ? schedule.getContent() : scheduleUpdateDto.getContent());
		schedule.setTitle(scheduleUpdateDto.getTitle() == "default" ? schedule.getTitle() : scheduleUpdateDto.getTitle());
		if(scheduleUpdateDto.getGoalDateTime() != null) {
			schedule.setYear(scheduleUpdateDto.getGoalDateTime().getYear());
			schedule.setMonth(scheduleUpdateDto.getGoalDateTime().getMonthValue());
			schedule.setDay(scheduleUpdateDto.getGoalDateTime().getDayOfMonth());
			boolean checkScheduel = scheduleUpdateDto.getGoalDateTime().isBefore(LocalDateTime.now()) ? true : false;
			schedule.setCheckScheduel(checkScheduel);
		}
		else {
			schedule.setYear(schedule.getYear());
			schedule.setMonth(schedule.getMonth());
			schedule.setDay(schedule.getDay());
			schedule.setCheckScheduel(schedule.isCheckScheduel());
		}
		schedule.setUser(schedule.getUser());
		return responseService.getSingleResult(scheduleRepository.save(schedule));
	}
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "일정 삭제", notes = "일정 삭제 하기")
	@DeleteMapping("/delete/{sid}")
	public CommonResult delete(
			@PathVariable("sid") long sid) {
		
		UserEntity user = authToUserService.getUser();
		if(user != scheduleRepository.getOne(sid).getUser()) {
			throw new CUserNotCorrectException();
		}
		scheduleRepository.deleteById(sid);
		return responseService.getSuccessResult();
	}
}
