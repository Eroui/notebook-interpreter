package com.notebook.interpreter.validation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class InterpreterRequestValidatorTest {

    private InterpreterRequestValidator interpreterRequestValidator;

    @Before
    public void setUp() {
        interpreterRequestValidator = Mockito.mock(InterpreterRequestValidator.class);
        Mockito.when(interpreterRequestValidator.matchesRequestPatternTest(Mockito.anyString())).thenCallRealMethod();
    }

    @Test
    public void validRequestPatternTest() {
        assertTrue(interpreterRequestValidator.matchesRequestPatternTest("%python "));
        assertTrue(interpreterRequestValidator.matchesRequestPatternTest("%python print(a + b)"));
        assertTrue(interpreterRequestValidator.matchesRequestPatternTest("%python  a = 1 + 2   "));
        assertTrue(interpreterRequestValidator.matchesRequestPatternTest("%java  System.out.println(\"Hello World\"); "));
        assertTrue(interpreterRequestValidator.matchesRequestPatternTest("%java  System.out.println(\"Hello World\");\\n" +
                "System.err.println(\"Hello Error World\");"));
    }

    @Test
    public void invalidRequestPatternTest() {
        assertFalse(interpreterRequestValidator.matchesRequestPatternTest("%python"));
        assertFalse(interpreterRequestValidator.matchesRequestPatternTest("% python print(a + b)"));
        assertFalse(interpreterRequestValidator.matchesRequestPatternTest(" %python  a = 1 + 2   "));
        assertFalse(interpreterRequestValidator.matchesRequestPatternTest(" python  a = 1 + 2   "));
        assertFalse(interpreterRequestValidator.matchesRequestPatternTest("java  System.out.println(\"Hello World\") "));

    }
}