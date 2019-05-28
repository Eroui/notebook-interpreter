package com.notebook.interpreter.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Language Not Supported")
public class LanguageNotSupportedException extends InterpreterException {

}
