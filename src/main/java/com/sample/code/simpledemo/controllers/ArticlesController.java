package com.sample.code.simpledemo.controllers;

import com.sample.code.simpledemo.repositories.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * @author dusan.grubjesic
 */
@RestController
@RequestMapping("/article")
public class ArticlesController {

	@Autowired
	private ArticlesRepository articlesRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/public")
	public List<Article> getAllArticles() {
		return articlesRepository.findAll();
	}

	@GetMapping("/my")
	public List<Article> getAllMyArticles(@Autowired Principal principal) {
		return articlesRepository.findAllByCreator(userRepository.getByUser(principal.getName()));
	}

	@PostMapping("/my")
	public int createArticle(@Autowired Principal principal, @Valid @RequestBody Article article) {
		User creator = userRepository.getByUser(principal.getName());
		article.setCreator(creator);
		article.setCreated(DateTime.now().toDate());
		return articlesRepository.save(article).getId();
	}

	@PatchMapping("/my/{id}")
	public Article createArticle(@PathVariable int id,
	                             @RequestBody ArticleMix articleMix) {
		Article changedArticle = articlesRepository.getById(id);
		if (articleMix.getName() != null) {
			changedArticle.setName(articleMix.getName());
		}
		if (articleMix.getText() != null) {
			changedArticle.setText(articleMix.getText());
		}
		if (articleMix.getImage() != null) {
			changedArticle.setImage(articleMix.getImage());
		}
		changedArticle.setModified(DateTime.now().toDate());
		return articlesRepository.save(changedArticle);
	}
}
