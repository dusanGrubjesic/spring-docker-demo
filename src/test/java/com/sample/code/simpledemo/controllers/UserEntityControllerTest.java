package com.sample.code.simpledemo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.code.simpledemo.repositories.UserEntity;
import com.sample.code.simpledemo.repositories.UserRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author dusan.grubjesic
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class UserEntityControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UserRepository userRepository;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void it_should_create_user() throws Exception {
		UserEntity ue = new UserEntity("user1", "pass1");
		when(userRepository.save(any())).thenReturn(ue);

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders.post("/user").content(objectMapper.writeValueAsString(ue)).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andReturn();
		UserEntity response = objectMapper.readValue(result.getResponse().getContentAsString(), UserEntity.class);

		Assertions.assertEquals(ue, response);
	}

	@Test
	@WithMockUser(username = "user1", password = "pass1", roles = "USER")
	public void it_should_get_user() throws Exception {
		UserEntity ue = new UserEntity("user1", "pass1");
		when(userRepository.getByUser(any())).thenReturn(ue);

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders.get("/user/me");
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andReturn();
		UserEntity response = objectMapper.readValue(result.getResponse().getContentAsString(), UserEntity.class);

		Assertions.assertEquals(ue, response);
	}

	@Test
	@WithMockUser(username = "user1", password = "pass1", roles = "USER")
	public void it_should_patch_user() throws Exception {
		UserEntity ue = new UserEntity("user12", "pass12");
		when(userRepository.save(any())).thenReturn(ue);

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders.patch("/user/me").content(objectMapper.writeValueAsString(ue)).contentType(MediaType.APPLICATION_JSON);;
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andReturn();
		UserEntity response = objectMapper.readValue(result.getResponse().getContentAsString(), UserEntity.class);

		Assertions.assertEquals(ue, response);
	}
}
