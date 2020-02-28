package com.rest.domain.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@Table(name = "schudule")
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEntity extends TimeEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sid;
	
	@Column(nullable = false)
	private LocalDateTime goalDateTime;
	
	@Column(nullable = false)
	private int year;
	
	@Column(nullable = false)
	private int month;
	
	@Column(nullable = false)
	private int day;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Column(length = 300)
	private String content;
	
//	@JsonBackReference
	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne
	@JoinColumn(name = "user_uid")
	private UserEntity user;

	@Column(nullable = false)
	@Builder.Default
	private boolean checkScheduel = false;
	
	@Column(nullable = false)
	private double beer;
	
	@Column(nullable = false)
	private double soju;
	
	@Column(nullable = true)
	private String imagePath;
	
}
