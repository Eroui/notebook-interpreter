package com.notebook.interpreter.model;

public enum Interpreter {
	PYTHON("python"),
	JAVA_SCRIPT("js");

	private String name;
	Interpreter(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static Interpreter getInterpreterFromLanguageName(String language) {
		for (Interpreter interpreter : Interpreter.values()) {
			if (interpreter.name.equals(language.toLowerCase().trim())) {
				return interpreter;
			}
		}

		return null; // add default ?
	}
}
