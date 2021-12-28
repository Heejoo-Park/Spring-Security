package com.heejoo.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.heejoo.security.model.User;

// CRUD 함수를 JpaRepository가 돌고 있음.
// @Repository라는 어노테이션이 없어도 IoC 됨
// ==> 이유는JpaRepository를 상속했기 때문에 
public interface UserRepository extends JpaRepository<User, Integer>{

	// findBy 규칙 --> Username 문법
	// select * from user where username = ? 호출 됨 
	public User findByUsername(String username); // Jpa Query method
	
}
