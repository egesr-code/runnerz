package dev.danvega.runnerz.run;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * Repositorio para gestionar operaciones CRUD de carreras (runs) en la base de datos.
 *
 * <p>Utiliza {@link JdbcClient} de Spring Framework 6 para ejecutar consultas SQL
 * de forma fluida y type-safe. JdbcClient es una alternativa moderna a JdbcTemplate
 * que ofrece una API más concisa y legible.</p>
 *
 * <p>Ejemplo de uso:</p>
 * <pre>{@code
 * // Obtener todas las carreras
 * List<Run> runs = runRepository.findAll();
 *
 * // Buscar por ID
 * Optional<Run> run = runRepository.findById(1);
 * }</pre>
 *
 * @author Dan Vega
 * @see Run
 * @see JdbcClient
 */
@Repository
public class RunRepository {

	private static final Logger log = LoggerFactory.getLogger(RunRepository.class);

	private final JdbcClient jdbcClient;

	/**
	 * Constructor que inyecta el cliente JDBC.
	 *
	 * @param jdbcClient cliente JDBC proporcionado por Spring para ejecutar consultas SQL
	 */
	public RunRepository (JdbcClient jdbcClient)
	{
		this.jdbcClient = jdbcClient;
	}

	/**
	 * Recupera todas las carreras de la base de datos.
	 *
	 * <p>Ejecuta: {@code SELECT * FROM run}</p>
	 *
	 * @return lista con todas las carreras, vacía si no hay registros
	 */
	public List<Run> findAll ()
	{
		return this.jdbcClient.sql("select * from run").query(Run.class).list();
	}

	/**
	 * Busca una carrera por su identificador único.
	 *
	 * <p>Ejecuta: {@code SELECT ... FROM run WHERE id = :id}</p>
	 *
	 * @param id identificador único de la carrera
	 * @return {@link Optional} con la carrera si existe, vacío si no se encuentra
	 */
	public Optional<Run> findById (Integer id)
	{
		return this.jdbcClient.sql("select id, title, started_on, completed_on, miles, location from run where id = :id").param("id", id).query(Run.class).optional();
	}

	/**
	 * Crea una nueva carrera en la base de datos.
	 *
	 * <p>Ejecuta: {@code INSERT INTO RUN (id, title, started_on, completed_on, miles, location) VALUES (?, ?, ?, ?, ?, ?)}</p>
	 *
	 * @param run objeto Run con los datos de la carrera a crear
	 * @throws IllegalStateException si la inserción falla (no se inserta exactamente 1 fila)
	 */
	public void create (Run run)
	{
		var updated = this.jdbcClient.sql("INSERT INTO RUN (id, title, started_on, completed_on, miles, location) VALUES (?, ?, ?, ?, ?, ?)")
				.params(List.of(run.id(), run.title(), run.startedOn(), run.completedOn(), run.miles(), run.location().toString())).update();

		Assert.state(updated == 1, "Failed to create run");
	}

	/**
	 * Actualiza una carrera existente en la base de datos.
	 *
	 * <p>Ejecuta: {@code UPDATE RUN SET title = ?, started_on = ?, completed_on = ?, miles = ?, location = ? WHERE id = ?}</p>
	 *
	 * @param run objeto Run con los nuevos datos de la carrera
	 * @param id identificador de la carrera a actualizar
	 * @throws IllegalStateException si la actualización falla (no se actualiza exactamente 1 fila)
	 */
	public void update (Run run, Integer id)
	{
		var updated = this.jdbcClient.sql("UPDATE RUN SET title = ?, started_on = ?, completed_on = ?, miles = ?, location = ? where id = ?")
				.params(run.title(), run.startedOn(), run.completedOn(), run.miles(), run.location().toString(), id).update();

		Assert.state(updated == 1, "Failed to update run");
	}

	/**
	 * Elimina una carrera de la base de datos.
	 *
	 * <p>Ejecuta: {@code DELETE FROM RUN WHERE id = :id}</p>
	 *
	 * @param id identificador de la carrera a eliminar
	 * @throws IllegalStateException si la eliminación falla (no se elimina exactamente 1 fila)
	 */
	public void delete (Integer id)
	{
		var updated = this.jdbcClient.sql("DELETE RUN WHERE id = :id")
				.param("id", id).update();

		Assert.state(updated == 1, "Failed to delete run");
	}

	/**
	 * Cuenta el número total de carreras en la base de datos.
	 *
	 * <p>Nota: Esta implementación recupera todas las filas y cuenta en memoria.
	 * Para mejor rendimiento en tablas grandes, considerar usar {@code SELECT COUNT(*)}.</p>
	 *
	 * @return número total de carreras
	 */
	public int count ()
	{
		return jdbcClient.sql("select * from run").query().listOfRows().size();
	}

	/**
	 * Guarda múltiples carreras en la base de datos.
	 *
	 * <p>Itera sobre la lista y llama a {@link #create(Run)} para cada elemento.
	 * Las inserciones se realizan de forma secuencial.</p>
	 *
	 * @param runs lista de carreras a guardar
	 * @throws IllegalStateException si alguna inserción falla
	 */
	public void saveAll (List<Run> runs)
	{
		runs.stream().forEach(this::create);
	}

	/**
	 * Busca carreras por ubicación (indoor/outdoor).
	 *
	 * <p>Ejecuta: {@code SELECT ... FROM run WHERE location = :location}</p>
	 *
	 * @param location ubicación a buscar ("INDOOR" o "OUTDOOR")
	 * @return lista de carreras que coinciden con la ubicación, vacía si no hay coincidencias
	 * @see Location
	 */
	public List<Run> findByLocation (String location)
	{
		return this.jdbcClient.sql("select id, title, started_on, completed_on, miles, location from run where location = :location").param("location", location).query(Run.class).list();
	}

	/**
	 * Recupera carreras con paginación.
	 *
	 * <p>Ejecuta: {@code SELECT * FROM run LIMIT :limit OFFSET :offset}</p>
	 *
	 * <p>Ejemplo: {@code findAll(0, 10)} retorna las primeras 10 carreras,
	 * {@code findAll(1, 10)} retorna las carreras 11-20.</p>
	 *
	 * @param page número de página (basado en 0)
	 * @param size cantidad de elementos por página
	 * @return lista de carreras para la página solicitada
	 */
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
