package com.rest.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.domain.entity.ScheduleEntity;
import com.rest.domain.entity.UserEntity;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
	List<ScheduleEntity> findByUser(UserEntity user);
	List<ScheduleEntity> findByUserAndYearAndMonth(UserEntity user, int year, int month);
}
