package com.notebook.interpreter.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InterpreterController {

    @PostMapping("/execute")
    public String execute() {
        return "";
    }
}
