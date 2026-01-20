package dev.danvega.runnerz.run;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

/**
 * Represents a running activity with its details.
 */
@Entity
@Table(name = "run")
public class Run {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotEmpty
	private String title;

	private LocalDateTime startedOn;

	private LocalDateTime completedOn;

	@Positive
	private Integer miles;

	@Enumerated(EnumType.STRING)
	private Location location;

	/**
	 * Default constructor required by JPA.
	 */
	public Run() {
	}

	/**
	 * Constructs a Run with all fields.
	 *
	 * @param id unique identifier for the run
	 * @param title descriptive title for the run (cannot be empty)
	 * @param startedOn date and time when the run started
	 * @param completedOn date and time when the run was completed
	 * @param miles distance covered in miles (must be positive)
	 * @param location where the run took place (indoor or outdoor)
	 * @throws IllegalArgumentException if completedOn is not after startedOn
	 */
	public Run(Integer id, String title, LocalDateTime startedOn, LocalDateTime completedOn, Integer miles, Location location) {
		if (completedOn != null && startedOn != null && !completedOn.isAfter(startedOn)) {
			throw new IllegalArgumentException("Completed on must be after Started on");
		}
		this.id = id;
		this.title = title;
		this.startedOn = startedOn;
		this.completedOn = completedOn;
		this.miles = miles;
		this.location = location;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDateTime getStartedOn() {
		return startedOn;
	}

	public void setStartedOn(LocalDateTime startedOn) {
		this.startedOn = startedOn;
	}

	public LocalDateTime getCompletedOn() {
		return completedOn;
	}

	public void setCompletedOn(LocalDateTime completedOn) {
		this.completedOn = completedOn;
	}

	public Integer getMiles() {
		return miles;
	}

	public void setMiles(Integer miles) {
		this.miles = miles;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
