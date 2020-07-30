package com.sample.code.simpledemo.controllers;

import com.sample.code.simpledemo.repositories.UserEntity;
import com.sample.code.simpledemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * @author dusan.grubjesic
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Endpoint returning user information, currently implemented name and password.
	 * Note: Bad practice for password to be returned but for demo purpose is included
	 * @param principal user extracted from basic auth
	 * @return {@link UserEntity} user entity of principal
	 */
	@GetMapping("/me")
	public UserEntity getUser(@Autowired Principal principal) {
		return userRepository.getByUser(principal.getName());
	}

	/**
	 * Endpoint changing {@link UserEntity} information, currently implemented name and password.
	 * Note: Bad practice for password to be changed like this but for demo purpose is ok
	 * Note: Bad practice for user to be changed like this. Better to set id as UUID and change only
	 * name but for demo purpose is ok
	 * @return changed user entity of principal
	 */
	@PatchMapping("/me")
	public UserEntity patchUser(@Autowired Principal principal,
	                            @Valid @RequestBody UserEntity changedUserEntity) {
		UserEntity userEntity = userRepository.getByUser(principal.getName());
		userEntity.setUser(changedUserEntity.getUser());
		userEntity.setPwd(passwordEncoder.encode(changedUserEntity.getPwd()));
		return userRepository.save(userEntity);
	}

	/**
	 * Creates user and password {@link UserEntity}. Password is encoded before saved to db
	 * Note: Bad practice for password to be returned but for demo purpose is included
	 * @param userEntity
	 * @return created user
	 */
	@PostMapping
	public UserEntity createUser(@Valid @RequestBody UserEntity userEntity) {
		userEntity.setPwd(passwordEncoder.encode(userEntity.getPwd()));
		return userRepository.save(userEntity);
	}
}
