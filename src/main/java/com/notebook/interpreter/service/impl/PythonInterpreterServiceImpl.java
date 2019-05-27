package com.notebook.interpreter.service.impl;

import com.notebook.interpreter.model.ExecutionBindings;
import com.notebook.interpreter.model.ExecutionRequest;
import com.notebook.interpreter.model.ExecutionResponse;
import com.notebook.interpreter.service.InterpreterService;
import org.graalvm.polyglot.Context;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PythonInterpreterServiceImpl implements InterpreterService {

    private static final String[] PERMITTED_LANGUAGES = {"js"};

    @Override
    public ExecutionResponse execute(ExecutionRequest request, ExecutionBindings bindings) {
        // TODO check is request language permitted
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errStream = new ByteArrayOutputStream();
        try (Context context = Context.newBuilder(PERMITTED_LANGUAGES).out(outputStream).err(errStream).build()) {
            for (String previousExecution : bindings.getPreviousExecutions()) {
                context.eval(request.getLanguage(), previousExecution);
            }

            // Empty both Streams
            outputStream.reset();
            errStream.reset();

            // TODO replace js by request language
            context.eval("js", request.getCode());

            bindings.addExecution(request.getCode());
            return new ExecutionResponse(outputStream.toString(), errStream.toString());
        }
    }
}
