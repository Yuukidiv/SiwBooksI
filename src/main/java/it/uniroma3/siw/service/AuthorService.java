package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.repository.AuthorRepository;

@Service
public class AuthorService {
	@Autowired
	private AuthorRepository authorRepository;
	
	public List<Author> getAllAuthors() {
		return (List<Author>) authorRepository.findAll();
	}
	
	public List<Author> getAllById(List<Long> id) {
		return (List<Author>) this.authorRepository.findAllById(id);
	}

	public Author getAuthorById(Long id) {
		return authorRepository.findById(id).orElse(null);
	}
	
	public void saveAuthor(Author author) {
		this.authorRepository.save(author);
	}
	
	public void deleteAuthor(Author author) {
		this.authorRepository.delete(author);
	}
	
	public boolean existsByNameAndSurnameAndDateOfBirth(String name, String surname, java.time.LocalDate dateOfBirth) {
		return authorRepository.existsByNameAndSurnameAndDateOfBirth(name, surname, dateOfBirth);
	}

}
