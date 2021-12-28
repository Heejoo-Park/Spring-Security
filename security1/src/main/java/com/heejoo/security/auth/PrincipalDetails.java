package com.heejoo.security.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.heejoo.security.model.User;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킴
// 로그인이 완료되면 session을 만들어줘야 함
// 일반적인 세션과 동일한 세션이긴 한데 시큐리티가 가지고 있는 세션이 따로 있음
// ==> Security ContextHolder 에 세션 정보를 저장시킴 
// Seucirty ContextHolder 에는 들어갈 수 있는 Object가 정해져 있음
// Object 에는 Authentication 타입 객
// 그리고 이 Authentication 안에는 User 정보가 있어야 함 
// 이 User Object 타입은 UserDetails 타입 객체임 

// Security Session <= Authentication 객체 <= UserDetails(=PrincipalDetails) 
public class PrincipalDetails implements UserDetails {
	
	private User user; // 컴퍼지션
		
	// 생성자 생성 
	public PrincipalDetails(User user) {
		this.user = user;
	}

	// 해당 User의 권한을 리턴하는 곳 
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// user.getRole(); 를 리턴해야하는데 얘는 String 타입이므로 리턴 불가
		// collection 에 담아서 리턴 
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				// TODO Auto-generated method stub
				return user.getRole();
			}
		});
		return collect;
		// return null; 일 경우, session이 deserialize 라고 반환함 
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUsername();
	}

	// 계정 존재 여부 
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정 락 여부 
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 계정 자격 증명 검증 
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 계정 활성화 여부 
	@Override
	public boolean isEnabled() {
		// 회원이 1년동안 로그인을 안하면 휴면계정 처리를 하기로 함 
		// 현재 시간 - 로그인 시간 > 1년 일 경우
		// return false; 처리 
		return true;
	}

	
}
