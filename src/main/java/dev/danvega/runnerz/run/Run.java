package dev.danvega.runnerz.run;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

/**
 * Represents a running activity with its details.
 *
 * @param id unique identifier for the run
 * @param title descriptive title for the run (cannot be empty)
 * @param startedOn date and time when the run started
 * @param completedOn date and time when the run was completed
 * @param miles distance covered in miles (must be positive)
 * @param location where the run took place (indoor or outdoor)
 */
public record Run(Integer id,
		@NotEmpty
		String title,
		LocalDateTime startedOn,
		LocalDateTime completedOn,
		@Positive
		Integer miles,
		Location location)
{

	/**
	 * Compact constructor that validates the run dates.
	 *
	 * @throws IllegalArgumentException if completedOn is not after startedOn
	 */
	public Run {
		if (!completedOn.isAfter(startedOn))
		{
			throw new IllegalArgumentException("Completed on must be after Started on");
		}
	}
}
