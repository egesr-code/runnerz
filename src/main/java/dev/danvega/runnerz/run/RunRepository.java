package dev.danvega.runnerz.run;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class RunRepository {

	private static final Logger log = LoggerFactory.getLogger(RunRepository.class);
	
	private final JdbcClient jdbcClient; 
	
	public RunRepository (JdbcClient jdbcClient)
	{
		this.jdbcClient = jdbcClient;
	}
	
	public List<Run> findAll ()
	{
		return this.jdbcClient.sql("select * from run").query(Run.class).list();
	}
	
	public Optional<Run> findById (Integer id)
	{
		return this.jdbcClient.sql("select id, title, started_on, completed_on, miles, location from run where id = :id").param("id", id).query(Run.class).optional();
	}
	
	public void create (Run run)
	{
		var updated = this.jdbcClient.sql("INSERT INTO RUN (id, title, started_on, completed_on, miles, location) VALUES (?, ?, ?, ?, ?, ?)")
				.params(List.of(run.id(), run.title(), run.startedOn(), run.completedOn(), run.miles(), run.location().toString())).update();
		
		Assert.state(updated == 1, "Failed to create run");
	}
	
	public void update (Run run, Integer id)
	{
		var updated = this.jdbcClient.sql("UPDATE RUN SET title = ?, started_on = ?, completed_on = ?, miles = ?, location = ? where id = ?")
				.params(run.title(), run.startedOn(), run.completedOn(), run.miles(), run.location().toString(), id).update();
		
		Assert.state(updated == 1, "Failed to update run");
	}
	
	public void delete (Integer id)
	{
		var updated = this.jdbcClient.sql("DELETE RUN WHERE id = :id")
				.param("id", id).update();
		
		Assert.state(updated == 1, "Failed to delete run");
	}
	
	public int count ()
	{
		return jdbcClient.sql("select * from run").query().listOfRows().size();
	}
	
	public void saveAll (List<Run> runs)
	{
		runs.stream().forEach(this::create);
	}
	
	public List<Run> findByLocation (String location)
	{
		return this.jdbcClient.sql("select id, title, started_on, completed_on, miles, location from run where location = :location").param("location", location).query(Run.class).list();
	}

	public List<Run> findAll(int page, int size)
	{
		int offset = page * size;
		return this.jdbcClient.sql("select * from run LIMIT :limit OFFSET :offset")
				.param("limit", size)
				.param("offset", offset)
				.query(Run.class)
				.list();
	}
}
