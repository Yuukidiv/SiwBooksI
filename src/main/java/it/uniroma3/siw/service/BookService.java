package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.repository.BookRepository;

@Service
public class BookService {
	@Autowired
	private BookRepository bookRepository;
	
	public List<Book> getAllBooks() {
		return (List<Book>) bookRepository.findAll();
	}

	public Book getBookById(Long id) {
		return bookRepository.findById(id).orElse(null);
	}
	// devo passare la lista filtrata in base all'autore I guess.
	
	public void saveBook(Book book) {
		this.bookRepository.save(book);
	}
	
	public void deleteBook(Long id) {
		this.bookRepository.deleteById(id);
	}
	public void deleteBook2(Book book) {
		this.bookRepository.delete(book);
	}

	public List<Book> searchBooks(String book, Integer dateOfPublication) {
		System.out.printf("Searching: title='%s', year=%s\n", book, dateOfPublication);
		return  (List<Book>) bookRepository.searchBooks(book, dateOfPublication);
	}
}
