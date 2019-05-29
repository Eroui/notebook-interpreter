package com.notebook.interpreter.service.impl;

import com.notebook.interpreter.model.ExecutionRequest;
import com.notebook.interpreter.model.InterpreterRequest;
import com.notebook.interpreter.model.exception.InvalidInterpreterRequestException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class InterpreterRequestParsingServiceImplTest {

    private InterpreterRequestParsingServiceImpl interpreterRequestParsingService;

    @Before
    public void setUp() throws InvalidInterpreterRequestException {
        interpreterRequestParsingService = Mockito.mock(InterpreterRequestParsingServiceImpl.class);
        Mockito.when(interpreterRequestParsingService.parseInterpreterRequest(Mockito.any(InterpreterRequest.class)))
                .thenCallRealMethod();
    }

    @Test
    public void parseInterpreterRequest() throws InvalidInterpreterRequestException {
        InterpreterRequest request = new InterpreterRequest();
        request.setCode("%js console.log('Hello World');");
        ExecutionRequest executionRequest = interpreterRequestParsingService.parseInterpreterRequest(request);
        assertEquals("js", executionRequest.getLanguage());
        assertEquals("console.log('Hello World');", executionRequest.getCode());
    }

    @Test(expected = InvalidInterpreterRequestException.class)
    public void parseInvalidInterpreterRequest() throws InvalidInterpreterRequestException {
        InterpreterRequest request = new InterpreterRequest();
        request.setCode(" %js console.log('Hello World');");
        interpreterRequestParsingService.parseInterpreterRequest(request);
    }
}