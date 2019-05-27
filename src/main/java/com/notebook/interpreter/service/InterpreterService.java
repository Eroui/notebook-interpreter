package com.notebook.interpreter.service;

import com.notebook.interpreter.model.ExecutionBindings;
import com.notebook.interpreter.model.ExecutionRequest;
import com.notebook.interpreter.model.ExecutionResponse;

public interface InterpreterService {

    ExecutionResponse execute(ExecutionRequest request, ExecutionBindings bindings);
}
