package com.notebook.interpreter.model.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.REQUEST_TIMEOUT, reason = "Execution request taking too long")
public class TimeOutException extends InterpreterException {}
