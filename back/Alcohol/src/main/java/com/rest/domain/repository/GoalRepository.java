package com.rest.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.domain.entity.GoalEntity;
import com.rest.domain.entity.UserEntity;

public interface GoalRepository extends JpaRepository<GoalEntity, Long> {
	List<GoalEntity> findByUser(UserEntity user);
}
