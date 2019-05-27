package com.notebook.interpreter.service;

import com.notebook.interpreter.model.exception.LanguageNotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InterpreterServiceFactory {

    private Map<String, InterpreterService> interpreterServiceMap;

    @Autowired
    public InterpreterServiceFactory(InterpreterService pythonInterpreterService) {
        interpreterServiceMap = new HashMap<>();
        interpreterServiceMap.put("python", pythonInterpreterService);
    }

    public InterpreterService getInterpreterService(String language) throws LanguageNotSupportedException {
        if (!interpreterServiceMap.containsKey(language)) {
            throw new LanguageNotSupportedException();
        }

        return interpreterServiceMap.get(language);
    }
}
