package com.sample.code.simpledemo.repositories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * @author dusan.grubjesic
 */
@Entity(name = "articles")
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;

	@CreatedBy
	@ManyToOne
	@JsonIgnore
	private UserEntity creator;

	@NotNull
	private String name;

	@NotNull
	private String text;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private Date created;

	@LastModifiedDate
	@Column(nullable = false)
	private Date modified;

	private byte[] image;
}
