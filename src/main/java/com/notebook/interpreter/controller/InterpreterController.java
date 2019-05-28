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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@Validated
public class InterpreterController {

    @Autowired
    private InterpreterRequestParsingService interpreterRequestParsingService;

    @Autowired
    private InterpreterServiceFactory interpreterServiceFactory;

    // Execuction Binding per Session
    @Autowired
    private ExecutionBindings executionBindings;

    @RequestMapping("/execute")
    public ResponseEntity<InterpreterResponse> execute(@CorrectRequest @RequestBody InterpreterRequest interpreterRequest, HttpSession httpSession) throws LanguageNotSupportedException {
        ExecutionRequest request = interpreterRequestParsingService.parseInterpreterRequest(interpreterRequest);
        InterpreterService interpreterService = interpreterServiceFactory.getInterpreterService(request.getLanguage());
        ExecutionResponse executionResponse = interpreterService.execute(request, executionBindings);
        InterpreterResponse interpreterResponse = new InterpreterResponse();
        interpreterResponse.setResponse(executionResponse.getOutput());


        return ResponseEntity.ok(interpreterResponse);
    }
}
