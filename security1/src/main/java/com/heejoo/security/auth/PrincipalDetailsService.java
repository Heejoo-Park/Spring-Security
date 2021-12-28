package com.heejoo.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.heejoo.security.model.User;
import com.heejoo.security.repository.UserRepository;

// 시큐리티 설정에서 loginProcessingUrl("/login")로 걸어놨기 때문에
// login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 
// loadByUserName 함수가 실행 
@Service
public class PrincipalDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	// String username 을 form 에 있는 name 과 매칭되어야 받아 올 수 있음 
	// form name 을 바꿀꺼면 SecurityConfig 에서 usernameParamater("이름")으로 바꿔야 
	// 이 함수는 UserDetails 로 리턴되어야 함 
	// 시큐리티 session(내부 Authentication(내부 UserDetails))
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("username : " + username);
		
		User userEntity = userRepository.findByUsername(username);
		System.out.println(userEntity.getRole());
		if(userEntity != null) {
			return new PrincipalDetails(userEntity);
		}
		
		return null;
	}

}
