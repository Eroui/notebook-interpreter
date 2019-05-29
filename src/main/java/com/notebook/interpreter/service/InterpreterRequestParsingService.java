package com.notebook.interpreter.service;

import com.notebook.interpreter.model.ExecutionRequest;
import com.notebook.interpreter.model.InterpreterRequest;
import com.notebook.interpreter.model.exception.InvalidInterpreterRequestException;

/**
 * Parse Interpreter request and extract needed information
 *
 * @author <a href="mailto:abdelaziz.eroui@gmail.com">Abdelaziz EROUI</a>
 */
public interface InterpreterRequestParsingService {
    /**
     * Extract execution request information from Interpreter request
     * @param request
     * @return
     */
    ExecutionRequest parseInterpreterRequest(InterpreterRequest request) throws InvalidInterpreterRequestException;
}
