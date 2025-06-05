package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Book;

public interface BookRepository extends CrudRepository<Book, Long> {

	 @Query("""
		        SELECT DISTINCT b FROM Book b
		        LEFT JOIN b.authors a
		        WHERE
		            (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND
		            (:dateOfPublication IS NULL OR b.dateOfPublication = :dateOfPublication)
		    """)
		List<Book> searchBooks(@Param("title") String title, @Param("dateOfPublication") Integer dateOfPublication);

	public boolean existsByTitleAndDateOfPublication(String title, Integer dateOfPublication);
}

