package com.notebook.interpreter.service.impl;

import com.notebook.interpreter.model.Interpreter;
import com.notebook.interpreter.service.GraalVmInterpreterService;
import org.springframework.stereotype.Service;

@Service
public class PythonInterpreterServiceImpl extends GraalVmInterpreterService {

    /**
     * {@inheritDoc}
     */
    @Override
    public Interpreter getInterpreterLanguage() {
        return Interpreter.PYTHON;
    }

}
