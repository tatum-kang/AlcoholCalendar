package com.rest.controller;

import java.time.LocalDate;
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
import io.swagger.annotations.ApiParam;
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
	@ApiOperation(value = "목표생성", notes = "목표를 생성")
	@PostMapping(value = "/save")
	public SingleResult<GoalEntity> save(
			@ApiParam(value = "시작날짜", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate startDate,
			@ApiParam(value = "끝날짜", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate endDate,
			@ApiParam(value = "목표 소주", required = true) @RequestParam(defaultValue = "0") double goalSoju,
			@ApiParam(value = "목표 맥주", required = true) @RequestParam(defaultValue = "0") double goalBeer,
			@ApiParam(value = "목표 제목", required = true) @RequestParam String title) {
		UserEntity user = authToUserService.getUser();
		GoalEntity goal = GoalEntity.builder().startDate(startDate).endDate(endDate).goalBeer(goalBeer)
				.goalSoju(goalSoju).title(title).user(user).build();
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
			goal.setDay(diff * -1);

			BeerSoju bs = calcBeer(user, goal.getStartDate());
			goal.setCurrentBeer(bs.beer);
			goal.setCurrentSoju(bs.soju);
		}
		return responseService.getListResult(goals);
	}

	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "목표수정", notes="목표를 수정")
	@PutMapping(value = "/update")
	public SingleResult<GoalEntity> update(
			@ApiParam(value = "시작날짜", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate startDate,
			@ApiParam(value = "끝날짜", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate endDate,
			@ApiParam(value = "목표 소주", required = true) @RequestParam(defaultValue = "0") double goalSoju,
			@ApiParam(value = "목표 맥주", required = true) @RequestParam(defaultValue = "0") double goalBeer,
			@ApiParam(value = "목표 제목", required = true) @RequestParam String title,
			@ApiParam(value = "목표 번호", required = true) @RequestParam long gid){
		UserEntity user = authToUserService.getUser();
		GoalEntity goal = GoalEntity.builder()
				.startDate(startDate)
				.endDate(endDate)
				.goalBeer(goalBeer)
				.goalSoju(goalSoju)
				.title(title)
				.user(user)
				.gid(gid)
				.build();
			return responseService.getSingleResult(goalRepository.save(goal));
	}
	
	@ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "목표삭제", notes="목표를 삭제")
	@DeleteMapping(value = "/delete")
	public CommonResult delete(@ApiParam(value = "목표 번호", required = true) @RequestParam long gid) {
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
