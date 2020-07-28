package com.sample.code.simpledemo.repositories;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	@JsonIgnore
	private String pwd;
}
