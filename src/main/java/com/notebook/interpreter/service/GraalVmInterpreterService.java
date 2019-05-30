package com.notebook.interpreter.service;

import com.notebook.interpreter.configuration.ApplicationProperties;
import com.notebook.interpreter.model.ExecutionContext;
import com.notebook.interpreter.model.ExecutionRequest;
import com.notebook.interpreter.model.ExecutionResponse;
import com.notebook.interpreter.model.exception.LanguageNotSupportedException;
import com.notebook.interpreter.model.exception.TimeOutException;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public abstract class GraalVmInterpreterService implements InterpreterService {

    @Autowired
    private ApplicationProperties applicationProperties;

    private Map<String, ExecutionContext> sessionContexts = new ConcurrentHashMap<>();
    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionResponse execute(ExecutionRequest request) {

        // Check if language supported
        if (unsupportedLanguage()) {
            throw new LanguageNotSupportedException();
        }

        ExecutionContext executionContext = getContext(request.getSessionId());
        final Context context = executionContext.getContext();

        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    context.close(true);
                    executionContext.getOutputStream().close();
                    executionContext.getErrorsStream().close();
                    executionContext.setTimedOut(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, applicationProperties.getTimeOutDuration());

        try {
            context.eval(getInterpreterLanguage().getName(), request.getCode());
            timer.cancel();
            timer.purge();
            return new ExecutionResponse(executionContext.getOutput(), executionContext.getErrors());
        } catch(PolyglotException e) {
            timer.cancel();
            timer.purge();
            if (e.isCancelled()) {
                // remove context
                sessionContexts.remove(request.getSessionId());
                throw new TimeOutException();
            }

            // TODO add polyglot exceptions handling ?
            return new ExecutionResponse("" , e.getMessage());
        }

    }

    private boolean unsupportedLanguage() {
        try (Context tmpContext = Context.create()) {
            return !tmpContext.getEngine().getLanguages().containsKey(getInterpreterLanguage().getName());
        }
    }

    /**
     * Get Execution Context by sessionId
     * @param sessionId
     * @return
     */
    private ExecutionContext getContext(String sessionId) {
        return sessionContexts.computeIfAbsent(sessionId, key -> buildContext());
    }

    private ExecutionContext buildContext() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorsStream = new ByteArrayOutputStream();
        Context context = Context.newBuilder(getInterpreterLanguage().getName()).out(outputStream).err(errorsStream)
                .build();

        return new ExecutionContext(outputStream, errorsStream, context);
    }
}
