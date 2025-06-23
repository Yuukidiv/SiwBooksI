package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {

	@Query("""
			    SELECT a FROM Author a
			    WHERE
			        (:name IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%')))
			        AND (:surname IS NULL OR LOWER(a.surname) LIKE LOWER(CONCAT('%', :surname, '%')))
			""")
	List<Author> searchAuthors(@Param("name") String name, @Param("surname") String surname);

	public boolean existsByNameAndSurnameAndDateOfBirth(String name, String surname, java.time.LocalDate dateOfBirth);
}
