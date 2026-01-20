package dev.danvega.runnerz;

import org.springframework.stereotype.Component;

/**
 * Component that provides welcome messages for the application.
 */
@Component
public class WelcomeMessage {

	/**
	 * Returns a welcome message for the application.
	 *
	 * @return the welcome message string
	 */
	public String getWelcomeMessage ()
	{
		return "Welcome to the Spring Boot Application!";
	}
}
