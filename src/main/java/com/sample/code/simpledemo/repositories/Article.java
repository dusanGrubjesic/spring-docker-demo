package com.sample.code.simpledemo.repositories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * @author dusan.grubjesic
 */
@Entity(name = "articles")
@Data
public class Article {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;

	@CreatedBy
	@ManyToOne
	@JsonIgnore
	private User creator;

	@NotNull
	private String name;

	@NotNull
	private String text;

	private Date created;

	private Date modified;

	private byte[] image;
}
