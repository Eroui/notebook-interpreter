package com.notebook.interpreter.service.impl;

import com.notebook.interpreter.InterpreterApplication;
import com.notebook.interpreter.model.ExecutionRequest;
import com.notebook.interpreter.model.ExecutionResponse;
import com.notebook.interpreter.model.exception.TimeOutException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InterpreterApplication.class)
public class JSInterpreterServiceImplTest {

	@Autowired
	private JSInterpreterServiceImpl jsInterpreterService;

	@Test
	public void testSimpleConsoleLog() {
		String helloWorld = "Hello World";
		ExecutionRequest request = new ExecutionRequest();
		request.setLanguage("js");
		request.setCode("console.log('"+ helloWorld + "');");
		request.setSessionId("mySessionId");

		ExecutionResponse response = jsInterpreterService.execute(request);
		assertTrue(response.getErrors().isEmpty());
		assertEquals(helloWorld + "\n", response.getOutput());
	}

	@Test
	public void testUndefinedVariableError() {
		ExecutionRequest request = new ExecutionRequest();
		request.setLanguage("js");
		request.setCode("console.log(a)");
		request.setSessionId("mySessionId");

		ExecutionResponse response = jsInterpreterService.execute(request);
		assertTrue(response.getOutput().isEmpty());
		assertEquals("ReferenceError: a is not defined", response.getErrors());
	}

	@Test
	public void testDefinedVariable() {
		ExecutionRequest request = new ExecutionRequest();
		request.setLanguage("js");
		request.setCode("var a = 5;");
		request.setSessionId("mySessionId");

		ExecutionResponse response1 = jsInterpreterService.execute(request);
		assertTrue(response1.getOutput().isEmpty());
		assertTrue(response1.getErrors().isEmpty());

		request.setCode("console.log(a);");
		ExecutionResponse response2 = jsInterpreterService.execute(request);
		assertEquals("5\n", response2.getOutput());
		assertTrue(response2.getErrors().isEmpty());
	}

	@Test
	public void testDefinedFunction() {
		String helloWorld = "Hello World";
		ExecutionRequest request = new ExecutionRequest();
		request.setLanguage("js");
		request.setCode("function f() { console.log('" + helloWorld + "') };");
		request.setSessionId("mySessionId");

		ExecutionResponse response1 = jsInterpreterService.execute(request);
		assertTrue(response1.getOutput().isEmpty());
		assertTrue(response1.getErrors().isEmpty());

		request.setCode("f();");
		ExecutionResponse response2 = jsInterpreterService.execute(request);
		assertEquals(helloWorld + '\n', response2.getOutput());
		assertTrue(response2.getErrors().isEmpty());
	}

	// TODO fake or force timeout ?
	// TODO test timeout duration ?
	@Test(expected = TimeOutException.class)
	public void testInfiniteLoop() {
		ExecutionRequest request = new ExecutionRequest();
		request.setLanguage("js");
		request.setCode("while(true);");
		request.setSessionId("mySessionId");

		jsInterpreterService.execute(request);
	}

	@Test
	public void testResetContext() {
		ExecutionRequest request = new ExecutionRequest();
		request.setLanguage("js");
		request.setCode("function f() { while(true) {console.log(5)} };");
		request.setSessionId("mySessionId");

		ExecutionResponse response1 = jsInterpreterService.execute(request);
		assertTrue(response1.getOutput().isEmpty());
		assertTrue(response1.getErrors().isEmpty());

		try {
			request.setCode("f();");
			jsInterpreterService.execute(request);
			fail(); // Should throw TIMEOUT Exception
		} catch (TimeOutException e) {}

		request.setCode("f();");
		ExecutionResponse response2 = jsInterpreterService.execute(request);
		assertTrue(response2.getOutput().isEmpty());
		assertEquals("ReferenceError: f is not defined", response2.getErrors());
	}

}