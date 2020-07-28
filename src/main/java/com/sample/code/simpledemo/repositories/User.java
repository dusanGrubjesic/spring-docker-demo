package com.sample.code.simpledemo.repositories;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * @author dusan.grubjesic
 */
@Data
@Entity(name = "users")
public class User {

	@Id
	@Column(name = "user_name")
	private String user;

	@NotNull
	@Column(name = "password")
	private String pwd;
}
