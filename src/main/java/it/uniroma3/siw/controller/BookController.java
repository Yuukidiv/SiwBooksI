package it.uniroma3.siw.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;

@Controller
public class BookController {
	
	@Autowired
	private BookService bookService;
	@Autowired
	private AuthorService authorService;
	
	 // homepage del sito
//	@GetMapping("/")
//	public String getHomepage() {
//		return "homepage.html";
//	}
	
	@GetMapping("/books")
	public String getBooks(Model model) {
		model.addAttribute("books", this.bookService.getAllBooks());
		return "books.html";
	}
	
	@GetMapping("/book/{id}")
	public String getBook(@PathVariable Long id, Model model) {
		model.addAttribute("book", this.bookService.getBookById(id));
		return "book.html";
	}

	@GetMapping("/books/newBook") 
	public String newBook(Model model) {
		model.addAttribute("book", new Book());
		model.addAttribute("authors", this.authorService.getAllAuthors());	// mi serve anche la lista di autori da inserire se voglio gia inserire tutto insieme 
		return "formNewBook.html";
		
	}
	// saving the book in the system with a POST METHOD CALL
	@PostMapping("/book") 
	public String saveBook(Model model, Book book) {
		/*System.out.println(">> data pubblicazione: " + book.getDateOfPublication());*/
		this.bookService.saveBook(book);
		return "redirect:book/" + book.getId();
	}
	
	
	@GetMapping("/edit/book/{id}")
	public String editBook(Model model, @PathVariable Long id) {
		model.addAttribute("book", this.bookService.getBookById(id));
		model.addAttribute("authors", this.authorService.getAllAuthors());
		return "editBook.html";
	}
	
	@PostMapping("/edited/book")
	public String saveEditBook(Book book, Model model) {
		this.bookService.saveBook(book);
		return "redirect:/book/" + book.getId();
		}
	
	@PostMapping("/delete/book/{id}")
	public String deleteBook(Model model, @PathVariable Long id) {
		Book book1 = this.bookService.getBookById(id);
		this.bookService.deleteBook2(book1);
		return "redirect:/books";
	}
	
}