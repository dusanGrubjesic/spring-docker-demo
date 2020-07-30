package com.sample.code.simpledemo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.code.simpledemo.repositories.Article;
import com.sample.code.simpledemo.repositories.ArticlesRepository;
import com.sample.code.simpledemo.repositories.UserEntity;
import com.sample.code.simpledemo.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author dusan.grubjesic
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ArticlesControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ArticlesRepository articlesRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(username = "user1", password = "pass1", roles = "USER")
	public void it_should_return_all_articles() throws Exception {
		UserEntity ue1 = new UserEntity(1, "user1", "pass1");
		UserEntity ue2 = new UserEntity(2, "user2", "pass2");
		userRepository.saveAll(Arrays.asList(ue1,ue2));

		Article a1 = new Article();
		a1.setCreator(ue1);
		a1.setName("header");
		a1.setText("text");
		Article a2 = new Article();
		a2.setCreator(ue2);
		a2.setName("header");
		a2.setText("text");

		List<Article> articles = Arrays.asList(a1,a2);
		articlesRepository.saveAll(articles);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/article/public");
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andReturn();
		List<Article> response = objectMapper.readValue(result.getResponse().getContentAsString(),
				objectMapper.getTypeFactory().constructCollectionType(List.class, Article.class));

		Assertions.assertEquals(articles.size(), response.size());
	}

	@Test
	@WithMockUser(username = "user1", password = "pass1", roles = "USER")
	public void it_should_return_my_articles() throws Exception {
		UserEntity ue1 = new UserEntity(1, "user1", "pass1");
		UserEntity ue2 = new UserEntity(2, "user2", "pass2");
		userRepository.saveAll(Arrays.asList(ue1,ue2));

		Article a1 = new Article();
		a1.setCreator(ue1);
		a1.setName("header");
		a1.setText("text");
		Article a2 = new Article();
		a2.setCreator(ue2);
		a2.setName("header");
		a2.setText("text");

		List<Article> articles = Arrays.asList(a1,a2);
		articlesRepository.saveAll(articles);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/article/my");
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andReturn();
		List<Article> response = objectMapper.readValue(result.getResponse().getContentAsString(),
				objectMapper.getTypeFactory().constructCollectionType(List.class, Article.class));

		Assertions.assertEquals(1, response.size());
	}

	@Test
	@WithMockUser(username = "user1", password = "pass1", roles = "USER")
	public void it_should_create_article() throws Exception {
		UserEntity ue = new UserEntity(1, "user1", "pass1");
		userRepository.save(ue);
		Article a1 = new Article();
		a1.setCreator(ue);
		a1.setName("header");
		a1.setText("this is text");

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/article/my")
				.content(objectMapper.writeValueAsString(a1))
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
		Assertions.assertEquals(1, articlesRepository.findAllByCreator(ue).size());
	}

	@Test
	@WithMockUser(username = "user1", password = "pass1", roles = "USER")
	public void change_user_after_article() throws Exception {
		UserEntity ue1 = new UserEntity(1, "user1", "pass1");
		userRepository.save(ue1);
		Article a1 = new Article();
		a1.setCreator(ue1);
		a1.setName("header");
		a1.setText("this is text");
		articlesRepository.save(a1);

		UserEntity ue2 = new UserEntity();
		ue2.setUser("user_new");
		ue2.setPwd("pass_new");


		//create user
		RequestBuilder requestBuilder1 = MockMvcRequestBuilders.post("/user")
				.content(objectMapper.writeValueAsString(ue1))
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder1);
		// create article
		RequestBuilder requestBuilder2 = MockMvcRequestBuilders.post("/article/my")
				.content(objectMapper.writeValueAsString(a1))
				.header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjE6cGFzczE=")
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder2);
		//patch user
		RequestBuilder requestBuilder3 =MockMvcRequestBuilders.patch("/user/me")
				.content(objectMapper.writeValueAsString(ue2))
				.header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjE6cGFzczE=")
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder3);

		//find user by new name and find articles
		UserEntity result = userRepository.getByUser("user_new");
		Assertions.assertEquals("header", articlesRepository.findAllByCreator(result).get(0).getName());
		Assertions.assertEquals("this is text", articlesRepository.findAllByCreator(result).get(0).getText());
	}

}
