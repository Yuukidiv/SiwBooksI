package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Genre;

public interface BookRepository extends CrudRepository<Book, Long> {

	@Query("""
		    SELECT DISTINCT b FROM Book b
		    LEFT JOIN b.authors a
		    WHERE
		        (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
		        AND (:dateOfPublication IS NULL OR b.dateOfPublication = :dateOfPublication)
		        AND (:genres IS NULL OR EXISTS (
		            SELECT 1 FROM b.genres g WHERE g IN :genres
		        ))
		""")
		List<Book> searchBooks(
		    @Param("title") String title,
		    @Param("dateOfPublication") Integer dateOfPublication,
		    @Param("genres") List<Genre> genres);


	public boolean existsByTitleAndDateOfPublication(String title, Integer dateOfPublication);

	List<Book> findByGenresContaining(Genre genre);
}

