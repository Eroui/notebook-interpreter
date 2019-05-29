package com.notebook.interpreter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notebook.interpreter.InterpreterApplication;
import com.notebook.interpreter.model.InterpreterRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InterpreterApplication.class)
@AutoConfigureMockMvc
public class InterpreterControllerTest {

	private static final String END_POINT = "/execute";

	@Autowired
	private MockMvc mvc;

	@Test
	public void invalidInterpreterRequest() throws Exception {
		InterpreterRequest request = new InterpreterRequest();
		request.setCode("invalid request code");
		mvc.perform(
				post(END_POINT)
				.content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void invalidInterpreterRequest2() throws Exception {
		InterpreterRequest request = new InterpreterRequest();
		request.setCode("% python my code");
		mvc.perform(
				post(END_POINT)
						.content(asJsonString(request))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void unsupportedLanguageInterpreterRequest() throws Exception {
		InterpreterRequest request = new InterpreterRequest();
		request.setCode("%php echo(\"Hello World\");");
		String message = mvc.perform(
				post(END_POINT)
						.content(asJsonString(request))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getErrorMessage();

		assertEquals("Language Not Supported", message);
	}

	// TODO add time out test

	public static String asJsonString(final Object obj) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(obj);
	}
}