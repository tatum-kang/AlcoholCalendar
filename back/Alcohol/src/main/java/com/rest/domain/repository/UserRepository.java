package com.rest.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.domain.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
	UserEntity findByNickname(String nickname);
}
