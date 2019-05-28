package com.notebook.interpreter.service;

import com.notebook.interpreter.model.ExecutionRequest;
import com.notebook.interpreter.model.ExecutionResponse;
import com.notebook.interpreter.model.exception.InterpreterException;
import com.notebook.interpreter.model.exception.LanguageNotSupportedException;
import com.notebook.interpreter.model.exception.TimeOutException;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public abstract class GraalVmInterpreterService implements InterpreterService {

    private Map<String, Context> sessionsBindings = new ConcurrentHashMap<>();

    @Override
    public ExecutionResponse execute(ExecutionRequest request) throws InterpreterException {

        // Check if language supported
        if (!Context.create().getEngine().getLanguages().containsKey(getInterpreterLanguage())) {
            throw new LanguageNotSupportedException();
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); ByteArrayOutputStream errStream = new ByteArrayOutputStream()){
            Context context = Context.newBuilder(getInterpreterLanguage()).out(outputStream).err(errStream).build();
            Value currentContextBinding = context.getBindings(getInterpreterLanguage());
            Context previousContext = getContext(request.getSessionId());

            if (previousContext != null) {
                Value previousBindings = previousContext.getBindings(getInterpreterLanguage());
                previousBindings.getMemberKeys().forEach(
                        key -> currentContextBinding.putMember(key, previousBindings.getMember(key))
                );
            }

            Timer timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    context.close(true);
                }
            }, 5000);

            context.eval(getInterpreterLanguage(), request.getCode());

            if (previousContext != null) {
                previousContext.close(); // closing previous context for replacement
            }

            putContext(request.getSessionId(), context);

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
        }

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
