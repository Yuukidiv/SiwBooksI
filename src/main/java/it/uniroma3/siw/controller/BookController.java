package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import it.uniroma3.siw.model.Genre;
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
		model.addAttribute("allGenres", Genre.values());
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
	public String saveBook(Model model, Book book, BindingResult bindingResult,
			@RequestParam("photo") MultipartFile photo,
			@RequestParam(name = "authorIds", required = false) List<Long> authorIds) throws IOException {

		if (authorIds != null && !authorIds.isEmpty()) {
			List<Author> selectedAuthors = authorService.getAllById(authorIds);
			book.setAuthors(selectedAuthors);
		}

		this.bookValidator.validate(book, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("error", "The book already exists in the system");
			model.addAttribute("authors", this.authorService.getAllAuthors());
			return "admin/formNewBook.html";
		} else {
			this.bookService.saveBook(book);
			Photo bookPhoto = new Photo();
			bookPhoto.setData(photo.getBytes());
			bookPhoto.setBook(book);
			this.photoService.savePhoto(bookPhoto);
			model.addAttribute("book", book);
			return "redirect:book/" + book.getId();
		}

	}

	@GetMapping("/admin/edit/book/{id}")
	public String editBook(Model model, @PathVariable Long id) {
		Book book = bookService.getBookById(id);
		List<Author> allAuthors = authorService.getAllAuthors();
		List<Author> selectedAuthors = book.getAuthors();
		List<Author> availableAuthors = new ArrayList<>(allAuthors);
		availableAuthors.removeAll(selectedAuthors);

		model.addAttribute("book", book);
		model.addAttribute("selectedAuthors", selectedAuthors);
		model.addAttribute("availableAuthors", availableAuthors);
		return "editBook.html";
	}

	@Transactional
	@PostMapping("/edited/book")
	public String saveEditBook(@RequestParam("id") Long id, @RequestParam("title") String title,
			@RequestParam("dateOfPublication") Integer date, @RequestParam("description") String description,
			@RequestParam(name = "authorIds", required = false) List<Long> authorIds,
			@RequestParam(name = "genres", required = false) Set<Genre> genres,
			@RequestParam(name = "photo", required = false) MultipartFile photoFile) throws IOException {

		Book book = bookService.getBookById(id);

		book.setTitle(title);
		book.setDateOfPublication(date);
		book.setDescription(description);
		book.setGenres(genres != null ? genres : new HashSet<>());

		List<Author> authors = (authorIds != null) ? authorService.getAllById(authorIds) : new ArrayList<>();
		book.setAuthors(authors);

		if (photoFile != null && !photoFile.isEmpty()) {
			// Se c'è una foto vecchia, la elimini
			Photo oldPhoto = book.getPhoto();
			if (oldPhoto != null) {
				book.setPhoto(null); // scollega la vecchia
				photoService.deletePhoto(oldPhoto);
			}

			// Salva il libro temporaneamente (per avere ID, stato managed, etc.)
			bookService.saveBook(book);

			// Crea e salva nuova foto
			Photo newPhoto = new Photo();
			newPhoto.setData(photoFile.getBytes());
			newPhoto.setBook(book);
			photoService.savePhoto(newPhoto);

			// Aggiorna relazione bidirezionale
			book.setPhoto(newPhoto);
		}

		bookService.saveBook(book); // Save finale

		return "redirect:/book/" + book.getId();
	}

	@GetMapping("/admin/deleteBook/{id}")
	public String deleteBook(Model model, @PathVariable Long id) {
		Book book1 = this.bookService.getBookById(id);
		this.bookService.deleteBook2(book1);
		return "redirect:/books";
	}

	@GetMapping("/books/results")
	public String foundBooks(@RequestParam(required = false) String title,
			@RequestParam(required = false) Integer dateOfPublication,
			@RequestParam(required = false) List<Genre> genres, @RequestParam(required = false) String author,
			Model model) {
		model.addAttribute("books", this.bookService.searchBooks(title, dateOfPublication, genres));

		model.addAttribute("title", title);

		model.addAttribute("genres", genres);
		model.addAttribute("allGenres", Genre.values());
		model.addAttribute("author", author);
		model.addAttribute("dateOfPublication", dateOfPublication);

		return "books.html";
	}

	@GetMapping("/books/genre/{genre}")
	public String booksByGenre(@PathVariable Genre genre, Model model) {
		List<Book> books = bookService.findBooksByGenre(genre);
		model.addAttribute("books", books);
		model.addAttribute("allGenres", Genre.values());
		model.addAttribute("genre", genre);
		return "books.html";
	}

}
