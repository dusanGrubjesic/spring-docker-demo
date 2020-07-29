package com.sample.code.simpledemo.controllers;

import com.sample.code.simpledemo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
		UserEntity creator = userRepository.getByUser(principal.getName());
		article.setCreator(creator);
		return articlesRepository.save(article).getId();
	}

	@PatchMapping("/my/{id}")
	public Article createArticle(@Autowired Principal principal,
	                             @PathVariable int id,
	                             @RequestBody ArticleMix articleMix) {
		Optional<Article> optChangedArticle =
				articlesRepository.findAllByCreator(userRepository.getByUser(principal.getName()))
						.stream()
						.filter(s -> s.getId() == id)
						.findFirst();
		if (!optChangedArticle.isPresent()) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Article Not Found");
		}
		Article changedArticle = optChangedArticle.get();
		if (articleMix.getName() != null) {
			changedArticle.setName(articleMix.getName());
		}
		if (articleMix.getText() != null) {
			changedArticle.setText(articleMix.getText());
		}
		if (articleMix.getImage() != null) {
			changedArticle.setImage(articleMix.getImage());
		}
		return articlesRepository.save(changedArticle);
	}
}
