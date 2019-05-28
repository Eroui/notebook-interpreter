package com.notebook.interpreter.service;

import com.notebook.interpreter.model.exception.LanguageNotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InterpreterServiceFactory {

    private Map<String, InterpreterService> interpreterServiceMap = new ConcurrentHashMap<>();

    @Autowired
    public InterpreterServiceFactory(List<InterpreterService> interpreterServices) {
        for (InterpreterService interpreterService: interpreterServices) {
            interpreterServiceMap.put(interpreterService.getInterpreterLanguage(), interpreterService);
        }
    }

    public InterpreterService getInterpreterService(String language) throws LanguageNotSupportedException {
        if (!interpreterServiceMap.containsKey(language)) {
            throw new LanguageNotSupportedException();
        }

        return interpreterServiceMap.get(language);
    }
}
