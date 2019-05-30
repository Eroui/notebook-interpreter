package com.notebook.interpreter.service.impl;

import com.notebook.interpreter.model.ExecutionRequest;
import com.notebook.interpreter.model.InterpreterRequest;
import com.notebook.interpreter.model.exception.InvalidInterpreterRequestException;
import com.notebook.interpreter.service.InterpreterRequestParsingService;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class InterpreterRequestParsingServiceImpl implements InterpreterRequestParsingService {

    private static final String REQUEST_PATTERN = "%(\\w+)\\s+(.*)";
    private static final Pattern pattern = Pattern.compile(REQUEST_PATTERN);

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionRequest parseInterpreterRequest(InterpreterRequest request) {
        Matcher matcher = pattern.matcher(request.getCode());
        if (matcher.matches()) {
            String language = matcher.group(1);
            String code = matcher.group(2);

            ExecutionRequest executionRequest = new ExecutionRequest();
            executionRequest.setCode(code);
            executionRequest.setLanguage(language);

            return executionRequest;
        }

        throw new InvalidInterpreterRequestException();
    }
}
