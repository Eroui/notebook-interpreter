package com.notebook.interpreter.service;

import com.notebook.interpreter.model.ExecutionRequest;
import com.notebook.interpreter.model.ExecutionResponse;
import com.notebook.interpreter.model.Interpreter;
import com.notebook.interpreter.model.exception.InterpreterException;

public interface InterpreterService {

    Interpreter getInterpreterLanguage();

    ExecutionResponse execute(ExecutionRequest request) throws InterpreterException;
}
