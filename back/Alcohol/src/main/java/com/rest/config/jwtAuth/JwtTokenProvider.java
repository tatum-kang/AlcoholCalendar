package com.rest.config.jwtAuth;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.rest.domain.entity.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@PropertySource("classpath:application.yml")
public class JwtTokenProvider {

	@Value("spring.jwt.secret")
	private String secretKey;

	private long tokenValidMilisecond = 1000L * 60 * 60;
	private final UserDetailsService userDetailsService;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public String createToken(String uid, List<String> roles) {
		Claims claims = Jwts.claims().setSubject(uid);
		claims.put("roles", roles);
		Date now = new Date();
		return Jwts.builder().setClaims(claims) // 데이터
				.setIssuedAt(now) // 토큰 발행일자
				.setExpiration(new Date(now.getTime() + tokenValidMilisecond)) // set Expire Time
				.signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
				.compact();
	}

	// Jwt 토큰에서 회원 구별 정보 추출
	public String getUserDetail(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}
	
	public Authentication getAuthentication(String token) {
		UserDetails user = userDetailsService.loadUserByUsername(this.getUserDetail(token));
		return new UsernamePasswordAuthenticationToken(user,"",user.getAuthorities());
	}

	// Request의 Header에서 token 파싱 : "X-AUTH-TOKEN: jwt토큰" jwt 토큰을 가지고 있나?
	public String resolveToken(HttpServletRequest req) {
		return req.getHeader("X-AUTH-TOKEN");
	}

	// Jwt 토큰의 유효성 + 만료일자 확인 토큰이 만료 되었는지?
	public boolean validateToken(String jwtToken) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}
}
