package com.sample.code.simpledemo.repositories;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author dusan.grubjesic
 */
@Data
@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;

	@Column(name = "user_name")
	private String user;

	@NotNull
	@Column(name = "password")
	private String pwd;

	@OneToMany(targetEntity = Article.class, cascade = CascadeType.ALL)
	private List<Article> articles;
}
