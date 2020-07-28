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

	@GetMapping("/me")
	public UserEntity getUser(@Autowired Principal principal) {
		return userRepository.getByUser(principal.getName());
	}

	@PatchMapping("/me")
	public UserEntity patchUser(@Autowired Principal principal, @Valid @RequestBody UserEntity userEntity) {
		userEntity.setPwd(passwordEncoder.encode(userEntity.getPwd()));
		userRepository.deleteById(principal.getName());
		return userRepository.save(userEntity);
	}

	@PostMapping
	public UserEntity createUser(@Valid @RequestBody UserEntity userEntity) {
		userEntity.setPwd(passwordEncoder.encode(userEntity.getPwd()));
		return userRepository.save(userEntity);
	}
}
