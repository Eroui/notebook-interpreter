package com.notebook.interpreter.service.impl;

import com.notebook.interpreter.model.ExecutionRequest;
import com.notebook.interpreter.model.ExecutionResponse;
import com.notebook.interpreter.service.InterpreterService;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PythonInterpreterServiceImpl implements InterpreterService {

    private static final String[] PERMITTED_LANGUAGES = {"js"};

    Map<String, Context> sessionsBindings;

    public PythonInterpreterServiceImpl() {
        sessionsBindings = new ConcurrentHashMap<>();
    }

    @Override
    public ExecutionResponse execute(ExecutionRequest request) {
        // TODO check is request language permitted
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errStream = new ByteArrayOutputStream();

        Context context = Context.newBuilder(PERMITTED_LANGUAGES).out(outputStream).err(errStream).build();
        Value currentContextBinding = context.getBindings("js");
        Context previousContext = getContext(request.getSessionId());

        if (previousContext != null) {
            Value previousBindings = previousContext.getBindings("js");
            previousBindings.getMemberKeys().forEach(
                    key -> currentContextBinding.putMember(key, previousBindings.getMember(key))
            );
        }

        try {
            context.eval("js", request.getCode());
        } catch (PolyglotException e) {
            return new ExecutionResponse(null , e.getMessage());
        }

        if (previousContext != null) {
            previousContext.close(); // closing previous context for replacement
        }

        putContext(request.getSessionId(), context);
        return new ExecutionResponse(outputStream.toString(), errStream.toString());

    }

    private Context getContext(String sessionId) {
        if (sessionId == null)
            return null;

        return sessionsBindings.get(sessionId);
    }

    private void putContext(String sessionId, Context context) {
        if (sessionId != null) sessionsBindings.put(sessionId, context);
    }
}
