package com.notebook.interpreter.service.impl;

import com.notebook.interpreter.model.ExecutionRequest;
import com.notebook.interpreter.model.InterpreterRequest;
import javafx.beans.property.IntegerProperty;
import jdk.internal.org.objectweb.asm.tree.analysis.Interpreter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class InterpreterRequestParsingServiceImplTest {

    private InterpreterRequestParsingServiceImpl interpreterRequestParsingService;

    @Before
    public void setUp() {
        interpreterRequestParsingService = Mockito.mock(InterpreterRequestParsingServiceImpl.class);
        Mockito.when(interpreterRequestParsingService.parseInterpreterRequest(Mockito.any(InterpreterRequest.class)))
                .thenCallRealMethod();
    }

    @Test
    public void parseInterpreterRequest() {
        InterpreterRequest request = new InterpreterRequest();
        request.setCode("%js console.log('Hello World');");
        ExecutionRequest executionRequest = interpreterRequestParsingService.parseInterpreterRequest(request);
        assertEquals("js", executionRequest.getLanguage());
        assertEquals("console.log('Hello World');", executionRequest.getCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseInvalidInterpreterRequest() {
        InterpreterRequest request = new InterpreterRequest();
        request.setCode(" %js console.log('Hello World');");
        interpreterRequestParsingService.parseInterpreterRequest(request);
    }
}