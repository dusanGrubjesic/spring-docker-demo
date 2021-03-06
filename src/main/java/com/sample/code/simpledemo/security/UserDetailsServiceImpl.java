package com.sample.code.simpledemo.security;

import com.sample.code.simpledemo.repositories.UserEntity;
import com.sample.code.simpledemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author dusan.grubjesic
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.getByUser(username);
		return new org.springframework.security.core.userdetails.User(userEntity.getUser(),
				userEntity.getPwd(), Collections.singletonList(new SimpleGrantedAuthority("USER")));
	}
}
