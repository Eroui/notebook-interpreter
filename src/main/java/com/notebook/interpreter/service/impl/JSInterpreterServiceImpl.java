package com.notebook.interpreter.service.impl;

import com.notebook.interpreter.service.GraalVmInterpreterService;
import org.springframework.stereotype.Service;

@Service
public class JSInterpreterServiceImpl extends GraalVmInterpreterService {

    private static final String INTERPRETER_LANGUAGE = "js";

    @Override
    public String getInterpreterLanguage() {
        return INTERPRETER_LANGUAGE;
    }
}
