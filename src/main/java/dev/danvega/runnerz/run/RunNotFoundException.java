package dev.danvega.runnerz.run;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested run is not found.
 * Results in an HTTP 404 Not Found response.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RunNotFoundException extends RuntimeException {

		/**
		 * Constructs a new RunNotFoundException with a default message.
		 */
		public RunNotFoundException() {
			super ("RUn not found");
		}
}
