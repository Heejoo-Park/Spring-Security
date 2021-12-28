package com.heejoo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration		// 메모리에 올리기 
@EnableWebSecurity  // 활성화 ==> 스프링 시큐리티 필터가 스프링 필터 체인에 등록이 됨 
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
// securedEnabled : secured 어노테이션 활성화 : 부여해준 ROLE만 접근 가능하게 함 
// prePostEnabled : preAuthorize 어노테이션, postAuthroize 어노테이션 활성화 
public class SecurityConfig extends WebSecurityConfigurerAdapter{ // 스프링 시큐리티 필터 설정

	// @Bean 어노테이션 
	// : 해당 메드의 리턴되는 오브젝트를 IoC로 리턴해줌 
	@Bean 
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		
		http.authorizeRequests()
			// 해당 주소로 들어오면 인증 필요해 라고 설정 
			// user는 인증만 되면 들어갈 수 있는 주소 
			.antMatchers("/user/**").authenticated()
			// 해당 주소로 들어오면 역할 인증받게 설정 
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			// 해당 주소로 들어오면 역할 인증받게 설정 
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			// 위에 3가지 주소 아니면 누구나 들어올 수 있도록 설정
			.anyRequest().permitAll()
		// 저 3가지 주소로 들어올 때는 login 페이지로 이동하도록 설정
		.and()
			.formLogin()
			.loginPage("/loginForm")
			// /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인 진행해줌 
			.loginProcessingUrl("/login") 
			// loginForm 페이지에서 login 성공시 들어가는 디폴트 페이지 설정 
			// 만약 특정 페이지 진입해서 login으로 들어왔을 경우, 그곳으로 redirect 
			.defaultSuccessUrl("/");
	}
}
