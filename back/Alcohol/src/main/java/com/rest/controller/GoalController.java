package com.rest.controller;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RestController;

import com.rest.advice.exception.CUserNotCorrectException;
import com.rest.domain.dto.GoalDto;
import com.rest.domain.dto.GoalUpdateDto;
import com.rest.domain.entity.GoalEntity;
import com.rest.domain.entity.ScheduleEntity;
import com.rest.domain.entity.UserEntity;
import com.rest.domain.repository.GoalRepository;
import com.rest.domain.repository.ScheduleRepository;
import com.rest.domain.response.CommonResult;
import com.rest.domain.response.ListResult;
import com.rest.domain.response.SingleResult;
import com.rest.service.AuthToUserService;
import com.rest.service.ResponseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@Api(tags = { "3. Goal" })
@RequiredArgsConstructor
@RequestMapping("/v1/Goal")
@Transactional
public class GoalController {
	private final GoalRepository goalRepository;
	private final ResponseService responseService;
	private final AuthToUserService authToUserService;
	private final ScheduleRepository scheduleRepository;

	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@ApiOperation(value = "목표생성", notes = "날짜양식 yy-MM-dd")
	@PostMapping(value = "/save")
	public SingleResult<GoalEntity> save(@RequestBody GoalDto goalDto) {
		UserEntity user = authToUserService.getUser();
		GoalEntity goal = goalDto.toEntity(user);
		return responseService.getSingleResult(goalRepository.save(goal));
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@ApiOperation(value = "목표조회", notes = "목표를 조회")
	@GetMapping(value = "/select")
	public ListResult<GoalEntity> select() {
		UserEntity user = authToUserService.getUser();
		List<GoalEntity> goals = goalRepository.findByUser(user);
		for (GoalEntity goal : goals) {
			LocalDate now = LocalDate.now();
			int diff = goal.getEndDate().getDayOfYear() - now.getDayOfYear();
			goal.setDday(diff * -1);

			BeerSoju bs = calcBeer(user, goal.getStartDate());
			goal.setCurrentBeer(bs.beer);
			goal.setCurrentSoju(bs.soju);
		}
		return responseService.getListResult(goals);
	}

	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "목표수정", notes="날짜 형식: yyyy-MM-dd, 업데이트하고자 하는 필드만 포함")
	@PutMapping(value = "/update/{gid}")
	public SingleResult<GoalEntity> update( @PathVariable("gid") long gid, @RequestBody GoalUpdateDto goalUpdateDto){
		UserEntity user = authToUserService.getUser();
		GoalEntity goal = goalRepository.getOne(gid);
		if(user != goal.getUser()) {
			throw new CUserNotCorrectException();
		}
		goal.setGoalBeer(goalUpdateDto.getGoalBeer() == -1 ? goal.getGoalBeer() : goalUpdateDto.getGoalBeer());
		goal.setGoalSoju(goalUpdateDto.getGoalSoju() == -1 ? goal.getGoalSoju() : goalUpdateDto.getGoalSoju());
		goal.setTitle(goalUpdateDto.getTitle() == "default" ? goal.getTitle() : goalUpdateDto.getTitle());
		goal.setStartDate(goalUpdateDto.getStartDate() == null ? goal.getStartDate() : goalUpdateDto.getStartDate());
		goal.setEndDate(goalUpdateDto.getEndDate() == null ? goal.getEndDate() : goalUpdateDto.getEndDate());
		return responseService.getSingleResult(goalRepository.save(goal));
	}
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "목표삭제", notes="목표를 삭제")
	@DeleteMapping(value = "/delete/{gid}")
	public CommonResult delete(@PathVariable("gid") long gid) {
		UserEntity user = authToUserService.getUser();
		if(user != goalRepository.getOne(gid).getUser()) {
			throw new CUserNotCorrectException();
		}
		goalRepository.deleteById(gid);
		return responseService.getSuccessResult();
	}
	

	private BeerSoju calcBeer(UserEntity user, LocalDate startDate) {
		List<ScheduleEntity> schedules = scheduleRepository.findByUserAndGoalDateTimeBetween(user,
				startDate.atTime(0, 0, 0), LocalDateTime.now());
		double beer = 0, soju = 0;
		for (ScheduleEntity schedule : schedules) {
			beer += schedule.getBeer();
			soju += schedule.getSoju();
		}
		return new BeerSoju(beer, soju);
	}
}

class BeerSoju {
	double beer, soju;

	public BeerSoju(double beer, double soju) {
		this.beer = beer;
		this.soju = soju;
	}
}
