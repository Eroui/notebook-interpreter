package com.notebook.interpreter.model;

public class ExecutionResponse {
    String output;
    String errors;

    public ExecutionResponse() {
        // no args constructor
    }

    public ExecutionResponse(String output, String errors) {
        this.output = output;
        this.errors = errors;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
