package com.sample.code.simpledemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author dusan.grubjesic
 */
public interface ArticlesRepository extends JpaRepository<Article, Integer> {

	Article findByIdAndAllByCreator(UserEntity creator);
}
