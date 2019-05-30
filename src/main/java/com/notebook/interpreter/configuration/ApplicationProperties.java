package com.notebook.interpreter.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "global-settings")
public class ApplicationProperties {

	private Long timeOutDuration;

	public Long getTimeOutDuration() {
		return timeOutDuration;
	}

	public void setTimeOutDuration(Long timeOutDuration) {
		this.timeOutDuration = timeOutDuration;
	}
}
