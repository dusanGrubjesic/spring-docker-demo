package com.sample.code.simpledemo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.code.simpledemo.repositories.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author dusan.grubjesic
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class ArticlesControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ArticlesRepository articlesRepository;
	@MockBean
	private UserRepository userRepository;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void it_should_return_all_articles() throws Exception {
		Article a1 = new Article();
		a1.setCreator(new UserEntity("user1", "pass1"));
		a1.setName("header");
		Article a2 = new Article();
		a2.setCreator(new UserEntity("user2", "pass2"));
		a2.setName("header");

		List<Article> articles = Arrays.asList(a1,a2);
		when(articlesRepository.findAll()).thenReturn(articles);

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
		UserEntity ue = new UserEntity("user1", "pass1");

		Article a1 = new Article();
		a1.setCreator(new UserEntity("user1", "pass1"));
		a1.setName("header");
		Article a2 = new Article();
		a2.setCreator(new UserEntity("user2", "pass2"));
		a2.setName("header");

		List<Article> articles = Arrays.asList(a1,a2);
		when(articlesRepository.findAll()).thenReturn(articles);
		when(articlesRepository.findAllByCreator(ue)).thenReturn(Collections.singletonList(a1));
		when(userRepository.getByUser(any())).thenReturn(ue);

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
		UserEntity ue = new UserEntity("user1", "pass1");

		Article a1 = new Article();
		a1.setCreator(new UserEntity("user1", "pass1"));
		a1.setName("header");
		a1.setText("this is text");
		a1.setId(23);

		when(articlesRepository.save(a1)).thenReturn(a1);
		when(userRepository.getByUser(any())).thenReturn(ue);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/article/my")
				.content(objectMapper.writeValueAsString(a1))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andReturn();
		Assertions.assertEquals(23, Integer.parseInt(result.getResponse().getContentAsString()));
	}
}
