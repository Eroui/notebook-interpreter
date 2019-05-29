package com.notebook.interpreter.service;

import com.notebook.interpreter.model.ExecutionContext;
import com.notebook.interpreter.model.ExecutionRequest;
import com.notebook.interpreter.model.ExecutionResponse;
import com.notebook.interpreter.model.exception.InterpreterException;
import com.notebook.interpreter.model.exception.LanguageNotSupportedException;
import com.notebook.interpreter.model.exception.TimeOutException;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public abstract class GraalVmInterpreterService implements InterpreterService {

    private Map<String, ExecutionContext> sessionsBindings = new ConcurrentHashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionResponse execute(ExecutionRequest request) throws InterpreterException {

        // Check if language supported
        if (!Context.create().getEngine().getLanguages().containsKey(getInterpreterLanguage().getName())) {
            throw new LanguageNotSupportedException();
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); ByteArrayOutputStream errStream = new ByteArrayOutputStream()){
            Context context = Context.newBuilder(getInterpreterLanguage().getName()).out(outputStream).err(errStream)
                    .build();

            ExecutionContext executionContext = getContext(request.getSessionId());
            if (executionContext != null && executionContext.getPreviousExecutions() != null) {
                for (String previousExecution : executionContext.getPreviousExecutions()) {
                    context.eval(getInterpreterLanguage().getName(), previousExecution);
                }
            }

            // Clear output and error stream from previous executions
            outputStream.reset();
            errStream.reset();

            Timer timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    context.close(true);
                }
            }, 5000);

            context.eval(getInterpreterLanguage().getName(), request.getCode());
            timer.cancel();

            if (executionContext != null) {
                executionContext.addExecution(request.getCode());
            }

            return new ExecutionResponse(outputStream.toString(), errStream.toString());
        } catch(PolyglotException e) {
            if (e.isCancelled()) {
                throw new TimeOutException();
            }

            // TODO add polyglot exceptions handling ?
            return new ExecutionResponse(null , e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return new ExecutionResponse(null, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Get Execution Context by sessionId
     * @param sessionId
     * @return
     */
    private ExecutionContext getContext(String sessionId) {
        if (sessionId == null)
            return new ExecutionContext();

        return sessionsBindings.computeIfAbsent(sessionId, key -> new ExecutionContext());
    }
}
