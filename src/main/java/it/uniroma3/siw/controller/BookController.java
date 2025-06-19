package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.BookValidator;
import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Photo;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.PhotoService;
import jakarta.transaction.Transactional;

@Controller
public class BookController {

	@Autowired
	private BookService bookService;
	@Autowired
	private AuthorService authorService;
	@Autowired 
	private PhotoService photoService;
	@Autowired
	private BookValidator bookValidator;

	@GetMapping("/books")
	public String getBooks(Model model) {
		model.addAttribute("books", this.bookService.getAllBooks());
		return "books.html";
	}

	@Transactional
	@GetMapping("/book/{id}")
	public String getBook(@PathVariable Long id, Model model) {
		Book book = this.bookService.getBookById(id);
		if (book == null) {
			return "error404.html";
		}
		model.addAttribute("book", this.bookService.getBookById(id));
		model.addAttribute("authors", book.getAuthors());
		model.addAttribute("reviews", book.getReviews());
		return "book.html";
	}

	@GetMapping("/admin/books/newBook")
	public String newBook(Model model) {
		model.addAttribute("book", new Book());
		model.addAttribute("authors", this.authorService.getAllAuthors());
		return "/admin/formNewBook.html";

	}

	// SAVING BOOK
	@PostMapping("/book")
	public String saveBook(Model model, Book book, BindingResult bindingResult, @RequestParam("photo") MultipartFile photo,
			@RequestParam(name ="authorIds", required = false) List<Long> authorIds) throws IOException {
		
		if (authorIds != null && !authorIds.isEmpty()) {
	        List<Author> selectedAuthors = authorService.getAllById(authorIds);
	        book.setAuthors(selectedAuthors);
	    }
		
		this.bookValidator.validate(book, bindingResult);
		if (book!=null) {
			this.bookService.saveBook(book); 
			Photo bookPhoto = new Photo();
            bookPhoto.setData(photo.getBytes());
            bookPhoto.setBook(book);
            this.photoService.savePhoto(bookPhoto);
			model.addAttribute("book", book);
			return "redirect:book/"+book.getId();
		} else {
			model.addAttribute("error", "The book already exists in the system");
			return "admin/formNewBook.html"; 
		}
		
	}

	@GetMapping("/admin/edit/book/{id}")
	public String editBook(Model model, @PathVariable Long id) {
		Book book = this.bookService.getBookById(id);
		model.addAttribute("book", book);
		// dovrei passare autori gia presenti e differenza con gli altri 
		model.addAttribute("bookAuthors", book.getAuthors());
		// qui dovrei passare magari un metodo nel service che mi ritorna la differenza tra i due
		model.addAttribute("authors", this.authorService.getAllAuthors());
		return "editBook.html";
	}

	@PostMapping("/edited/book")
	public String saveEditBook(Book book, Model model,  @RequestParam(name ="authorIds", required = false) List<Long> authorIds ) {
		if (authorIds != null && !authorIds.isEmpty()) {
	        List<Author> selectedAuthors = authorService.getAllById(authorIds);
	        book.setAuthors(selectedAuthors);
	    }
		this.bookService.saveBook(book);
		return "redirect:/book/" + book.getId();
	}

	@GetMapping("/admin/deleteBook/{id}")
	public String deleteBook(Model model, @PathVariable Long id) {
		Book book1 = this.bookService.getBookById(id);
		this.bookService.deleteBook2(book1);
		return "redirect:/books";
	}


	@GetMapping("/books/results") 
	public String foundBooks(@RequestParam(required =
			false) String title, @RequestParam(required = false) Integer dateOfPublication,  Model model) {
		System.out.println("Titolo ricevuto: " + title);
	    System.out.println("Anno di pubblicazione ricevuto: " + dateOfPublication);
		model.addAttribute("books", this.bookService.searchBooks(title, dateOfPublication)); 
		model.addAttribute("title", title);
		// model.addAttribute("author", author);
		model.addAttribute("dateOfPublication", dateOfPublication);
		return "books.html"; }

}