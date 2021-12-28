package com.heejoo.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.heejoo.security.model.User;
import com.heejoo.security.repository.UserRepository;

@Controller
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping({"", "/"})
	public String index() {
		// Mustache 기본 폴더 src/main/resouces/
		// ViewResolver 설정 : templates (prefix) / mustache (suffix)
		
		// src/main/resouces/templates/index.mustache로 잡힘 
		return "index"; 
		// 경로 설정이  잘못되어있으므로 WegbMvcConfig 에서 설정 다시 잡아줌 
	}
	
	@GetMapping("/user")
	@ResponseBody
	public String user() {
		return "user";
	}
	
	@GetMapping("/admin")
	@ResponseBody
	public String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	@ResponseBody
	public String manager() {
		return "manager";
	}
	
	// login 으로 해놓으면 스프링 Security가 인터셉트 함 
	// SecurityConfig 파일 생성 후 작동 안함 
	// --> loginForm 설정하면 다시 인터셉트함
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}

	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user);
		
		// Role 설정
		user.setRole("ROLE_USER");
		// 회원가입 잘 됨 --> 비밀번호 : 1234
		// 시큐리티로 로그인을 할 수 없음 --> 이유 : 패스워드가 암호화가 안되어있기 때문		
		String rawPassword = user.getPassword();
		// 시큐리티 암호화 설정 
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		
		user.setPassword(encPassword);
		System.out.println(user);
		
		userRepository.save(user);
		return "redirect:/loginForm";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	// @PreAuthorize : 이 데이터라는 method 가 실행되기 직전에 실행됨 
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	// @PostAuthorize : 이 메소드 끝나고 난 뒤에 실행됨 
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "매니저 및 어드민용 데이터";
	}
}
