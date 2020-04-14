package com.rest.domain.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "goal")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalEntity extends TimeEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long gid;
	
	@Column(nullable = false)
	private LocalDate endDate;
	
	@Column(nullable = false)
	private LocalDate startDate;
	
	@Column(nullable = true)
	private double currentBeer;
	
	@Column(nullable = true)
	private double currentSoju;
	
	@Column(nullable = false)
	private double goalBeer;
	
	@Column(nullable = false)
	private double goalSoju;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Column(nullable = true)
	private int dday;
	
	@JsonManagedReference
	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_uid")
	private UserEntity user;
}
