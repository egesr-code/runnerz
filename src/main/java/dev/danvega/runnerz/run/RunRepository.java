package dev.danvega.runnerz.run;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar operaciones CRUD de carreras (runs) en la base de datos.
 *
 * <p>Utiliza Spring Data JPA con {@link ListCrudRepository} para proporcionar
 * operaciones CRUD automáticas.</p>
 *
 * @author Dan Vega
 * @see Run
 */
@Repository
public interface RunRepository extends ListCrudRepository<Run, Integer> {

	/**
	 * Busca carreras por ubicación (indoor/outdoor).
	 *
	 * @param location ubicación a buscar (INDOOR o OUTDOOR)
	 * @return lista de carreras que coinciden con la ubicación
	 * @see Location
	 */
	List<Run> findByLocation(Location location);

	/**
	 * Recupera carreras con paginación.
	 *
	 * @param pageable objeto de paginación con página y tamaño
	 * @return página de carreras
	 */
	Page<Run> findAll(Pageable pageable);
}
