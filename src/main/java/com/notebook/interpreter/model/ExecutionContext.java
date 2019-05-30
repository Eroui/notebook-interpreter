package com.notebook.interpreter.model;

import org.graalvm.polyglot.Context;

import java.io.ByteArrayOutputStream;

public class ExecutionContext {

    private ByteArrayOutputStream outputStream;
    private ByteArrayOutputStream errorsStream;
    private Context context;
    private boolean timedOut = false;

    public ExecutionContext(ByteArrayOutputStream outputStream, ByteArrayOutputStream errorsStream, Context context) {
        this.outputStream = outputStream;
        this.errorsStream = errorsStream;
        this.context = context;
    }

    public ByteArrayOutputStream getOutputStream() {
        return outputStream;
    }

    public ByteArrayOutputStream getErrorsStream() {
        return errorsStream;
    }

    public void setOutputStream(ByteArrayOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setErrorsStream(ByteArrayOutputStream errorsStream) {
        this.errorsStream = errorsStream;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isTimedOut() {
        return timedOut;
    }

    public void setTimedOut(boolean timedOut) {
        this.timedOut = timedOut;
    }

    public String getOutput() {
        String out = this.outputStream.toString();
        this.outputStream.reset();
        return out;
    }


    public String getErrors() {
        String out = this.errorsStream.toString();
        this.errorsStream.reset();
        return out;
    }

}
