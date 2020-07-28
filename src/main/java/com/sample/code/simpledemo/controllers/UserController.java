package com.sample.code.simpledemo.controllers;

import com.sample.code.simpledemo.repositories.User;
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
	public User getUser(@Autowired Principal principal) {
		return userRepository.getByUser(principal.getName());
	}

	@PatchMapping("/me")
	public User patchUser(@Autowired Principal principal, @Valid @RequestBody User user) {
		user.setPwd(passwordEncoder.encode(user.getPwd()));
		userRepository.deleteById(principal.getName());
		return userRepository.save(user);
	}

	@PostMapping
	public User createUser(@Valid @RequestBody User user) {
		user.setPwd(passwordEncoder.encode(user.getPwd()));
		return userRepository.save(user);
	}
}
