package com.rest.domain.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@Setter
public class UserEntity extends TimeEntity implements UserDetails{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uid;
	
	@Column(length = 100, nullable = false, unique = true)
	private String email;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(length = 100, nullable = false)
	private String password;
	
	@Column(length = 100, nullable = false)
	private String name;
	
	@Column(length = 100, nullable = false, unique = true)
	private String nickname;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@Builder.Default
	private List<String> roles = new ArrayList<>();
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(length = 100, nullable = false)
	private String emailAuthKey;
	
	@Column(nullable = false)
	private boolean emailAuthBool;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@Builder.Default
	private List<ScheduleEntity> schedules = new ArrayList<>();
	
	
	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@Builder.Default
	private List<GoalEntity> goals = new ArrayList<>();
	
	
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	@Override
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public String getPassword() {
		return this.password;
	}

	@Override
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public String getUsername() {
		return this.email;
	}

	@Override
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public boolean isEnabled() {
		return true;
	}

}
