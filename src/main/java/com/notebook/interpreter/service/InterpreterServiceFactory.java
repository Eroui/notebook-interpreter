package com.notebook.interpreter.service;

import com.notebook.interpreter.model.Interpreter;
import com.notebook.interpreter.model.exception.LanguageNotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Factory for all Interpreter Services
 *
 * @author <a href="mailto:abdelaziz.eroui@gmail.com">Abdelaziz EROUI</a>
 */
@Service
public class InterpreterServiceFactory {

    // TODO use enum map
    private Map<Interpreter, InterpreterService> interpreterServiceMap = new EnumMap<>(Interpreter.class);

    @Autowired
    public InterpreterServiceFactory(List<InterpreterService> interpreterServices) {
        for (InterpreterService interpreterService: interpreterServices) {
            interpreterServiceMap.put(interpreterService.getInterpreterLanguage(), interpreterService);
        }
    }

    /**
     * get Interpreter Service from language
     * @param language
     * @return
     * @throws LanguageNotSupportedException in case no interpreter service mapped to the given language
     */
    public InterpreterService getInterpreterService(String language) {
        Interpreter interpreter = Interpreter.getInterpreterFromLanguageName(language);
        if (interpreter == null || !interpreterServiceMap.containsKey(interpreter)) {
            throw new LanguageNotSupportedException();
        }
        return interpreterServiceMap.get(interpreter);
    }
}
