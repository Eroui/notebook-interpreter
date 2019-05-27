package com.notebook.interpreter.service;

import com.notebook.interpreter.model.ExecutionRequest;
import com.notebook.interpreter.model.InterpreterRequest;

public interface InterpreterRequestParsingService {
    ExecutionRequest parseInterpreterRequest(InterpreterRequest request);
}
