package com.sample.code.simpledemo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.code.simpledemo.repositories.UserEntity;
import com.sample.code.simpledemo.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author dusan.grubjesic
 */

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserEntityControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void it_should_create_user() throws Exception {
		UserEntity ue = new UserEntity();
		ue.setUser("user1");
		ue.setPwd("pass1");

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders.post("/user").content(objectMapper.writeValueAsString(ue)).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andReturn();
		UserEntity response = objectMapper.readValue(result.getResponse().getContentAsString(), UserEntity.class);

		Assertions.assertEquals(ue.getUser(), response.getUser());
	}

	@Test
	@WithMockUser(username = "user1", password = "pass1", roles = "USER")
	public void it_should_get_user() throws Exception {
		UserEntity ue = new UserEntity();
		ue.setUser("user1");
		ue.setPwd("pass1");
		userRepository.save(ue);

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders.get("/user/me");
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andReturn();
		UserEntity response = objectMapper.readValue(result.getResponse().getContentAsString(), UserEntity.class);

		Assertions.assertEquals("user1", response.getUser());
	}

	@Test
	@WithMockUser(username = "user1", password = "pass1", roles = "USER")
	public void it_should_patch_user() throws Exception {
		UserEntity ue = new UserEntity(1, "user1", "pass1");
		userRepository.save(ue);

		UserEntity patch = new UserEntity();
		patch.setUser("user_new");
		patch.setPwd("pass_new");

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders.patch("/user/me").content(objectMapper.writeValueAsString(patch)).contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(status().isOk());

		Assertions.assertEquals(1, userRepository.getByUser("user_new").getId());
		Assertions.assertNull(userRepository.getByUser("user1"));
	}
}
