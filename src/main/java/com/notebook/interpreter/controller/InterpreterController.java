package com.notebook.interpreter.controller;

import com.notebook.interpreter.model.InterpreterRequest;
import com.notebook.interpreter.model.InterpreterResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InterpreterController {

    @PostMapping("/execute")
    public ResponseEntity<InterpreterResponse> execute(@RequestBody InterpreterRequest interpreterRequest) {
        InterpreterResponse interpreterResponse = new InterpreterResponse();
        interpreterResponse.setResponse(interpreterRequest.getCode());
        return ResponseEntity.ok(interpreterResponse);
    }
}
