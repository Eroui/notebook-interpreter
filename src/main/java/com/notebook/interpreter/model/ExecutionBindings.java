package com.notebook.interpreter.model;

import java.util.ArrayList;
import java.util.List;

// TODO manage per sessions
// TODO serialisable for storage?
public class ExecutionBindings {
    List<String> previousExecutions;

    public ExecutionBindings() {
        previousExecutions = new ArrayList<>();
    }

    public void addExecution(String execution) {
        previousExecutions.add(execution);
    }

    public List<String> getPreviousExecutions() {
        if (previousExecutions == null) {
            // To avoid null pointer exceptions ^_^
            return previousExecutions = new ArrayList<>();
        }
        return previousExecutions;
    }

    public void setPreviousExecutions(List<String> previousExecutions) {
        this.previousExecutions = previousExecutions;
    }
}
