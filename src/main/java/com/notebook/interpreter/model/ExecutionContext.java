package com.notebook.interpreter.model;

import java.util.ArrayList;
import java.util.List;

public class ExecutionContext {
    List<String> previousExecutions;

    public ExecutionContext() {
        previousExecutions = new ArrayList<>();
    }

    public void addExecution(String execution) {
        this.previousExecutions.add(execution);
    }

    public List<String> getPreviousExecutions() {
        if (this.previousExecutions == null) {
            return new ArrayList<>();
        }
        return previousExecutions;
    }
}
