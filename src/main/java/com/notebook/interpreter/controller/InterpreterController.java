package com.notebook.interpreter.controller;

import com.notebook.interpreter.model.*;
import com.notebook.interpreter.model.exception.LanguageNotSupportedException;
import com.notebook.interpreter.service.InterpreterRequestParsingService;
import com.notebook.interpreter.service.InterpreterService;
import com.notebook.interpreter.service.InterpreterServiceFactory;
import com.notebook.interpreter.validation.CorrectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class InterpreterController {

    @Autowired
    private InterpreterRequestParsingService interpreterRequestParsingService;

    @Autowired
    private InterpreterServiceFactory interpreterServiceFactory;

    @PostMapping("/execute")
    public ResponseEntity<InterpreterResponse> execute(@CorrectRequest @RequestBody InterpreterRequest interpreterRequest) throws LanguageNotSupportedException {
        ExecutionRequest request = interpreterRequestParsingService.parseInterpreterRequest(interpreterRequest);

        InterpreterService interpreterService = interpreterServiceFactory.getInterpreterService(request.getLanguage());
        // TODO store execution binding somewhere
        ExecutionResponse executionResponse = interpreterService.execute(request, new ExecutionBindings());
        InterpreterResponse interpreterResponse = new InterpreterResponse();
        interpreterResponse.setResponse(executionResponse.getOutput());

        return ResponseEntity.ok(interpreterResponse);
    }
}
