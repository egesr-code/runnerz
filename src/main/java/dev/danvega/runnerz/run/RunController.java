package dev.danvega.runnerz.run;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * REST controller for managing running activities.
 * Provides CRUD endpoints for runs at /api/runs.
 */
@RestController
@RequestMapping("/api/runs")
public class RunController {


	private final RunRepository runRepository;

	/**
	 * Constructs a RunController with the specified repository.
	 *
	 * @param runRepository the repository for run data access
	 */
	public RunController(RunRepository runRepository) {
		this.runRepository = runRepository;
	}
	
	/**
	 * Retrieves all runs from the database.
	 *
	 * @return a list of all runs
	 */
	@GetMapping("")
	public List<Run> findAll ()
	{
		return runRepository.findAll();
	}

	/**
	 * Retrieves a paginated list of runs.
	 *
	 * @param page the page number (zero-based), defaults to 0
	 * @param size the number of elements per page, defaults to 10
	 * @return a paginated response containing runs and pagination metadata
	 */
	@GetMapping("/paged")
	public PagedResponse<Run> findAllPaged(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size)
	{
		List<Run> runs = runRepository.findAll(page, size);
		int totalElements = runRepository.count();
		return new PagedResponse<>(runs, page, size, totalElements);
	}

	/**
	 * Retrieves a specific run by its identifier.
	 *
	 * @param id the unique identifier of the run
	 * @return the run with the specified id
	 * @throws RunNotFoundException if no run exists with the given id
	 */
	@GetMapping("/{id}")
	public Run findById(@PathVariable Integer id)
	{
		Optional<Run> run = runRepository.findById(id);

		if (run.isEmpty())
			throw new RunNotFoundException();

		return run.get();
	}

	/**
	 * Creates a new run in the database.
	 *
	 * @param run the run data to create (validated)
	 */
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("")
	public void create (@Valid @RequestBody Run run)
	{
		runRepository.create(run);
	}


	/**
	 * Updates an existing run with the specified id.
	 *
	 * @param run the updated run data (validated)
	 * @param id the unique identifier of the run to update
	 */
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping("/{id}")
	public void update (@Valid  @RequestBody Run run, @PathVariable  Integer id)
	{
		runRepository.update(run, id);
	}

	/**
	 * Deletes a run by its identifier.
	 *
	 * @param id the unique identifier of the run to delete
	 */
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete (@PathVariable Integer id)
	{
		runRepository.delete(id);
	}
	
}
