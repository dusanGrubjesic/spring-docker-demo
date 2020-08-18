package com.sample.code.simpledemo.controllers;

import com.sample.code.simpledemo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Comparator;
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

	/**
	 * @return Gets all public articles. For demo only public articles exist
	 */
	@GetMapping("/public")
	public List<Article> getAllArticles() {
		return articlesRepository.findAll();
	}

	/**
	 * Returns articles of requesting user
	 * @param principal
	 * @return List of all articles writen by user
	 */
	@GetMapping("/my")
	public List<Article> getAllMyArticles(@Autowired Principal principal) {
		return userRepository.getByUser(principal.getName()).getArticles();
	}

	/**
	 * Creates article and sets principle as creator of article
	 * @param principal
	 * @param article
	 * @return
	 */
	@PostMapping("/my")
	public int createArticle(@Autowired Principal principal, @Valid @RequestBody Article article) {
		UserEntity creator = userRepository.getByUser(principal.getName());
		creator.getArticles().add(article);
		return userRepository.save(creator).getArticles().stream()
				.min(Comparator.comparingInt(Article::getId))
				.get()
				.getId();
	}

	/**
	 * First check if article with param id exists and changes any one of three changeable fields: name,
	 * text and image
	 * Rest response ignores {@link UserEntity} to showcase practice of not returning password information
	 * @param principal
	 * @param id
	 * @param article Article without bean validation
	 * @return newly changed article
	 */
	@PatchMapping("/my/{id}")
	public Article createArticle(@Autowired Principal principal,
	                             @PathVariable int id,
	                             @RequestBody Article article) {
		Optional<Article> optChangedArticle = Optional.ofNullable(
				userRepository.getByUser(principal.getName()).getArticles().get(id));
		if (!optChangedArticle.isPresent()) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Article Not Found");
		}
		Article changedArticle = optChangedArticle.get();
		if (article.getName() != null) {
			changedArticle.setName(article.getName());
		}
		if (article.getText() != null) {
			changedArticle.setText(article.getText());
		}
		if (article.getImage() != null) {
			changedArticle.setImage(article.getImage());
		}
		return articlesRepository.save(changedArticle);
	}
}
