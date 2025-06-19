package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.List;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Photo;
import it.uniroma3.siw.controller.validator.AuthorValidator;
import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.PhotoService;
import jakarta.transaction.Transactional;

@Controller
public class AuthorController {

	@Autowired
	private AuthorService authorService;
	
	@Autowired 
	private PhotoService photoService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private AuthorValidator authorValidator;
	
	@GetMapping("/authors")
	public String getAllAuthors(Model model) {
		model.addAttribute("authors", this.authorService.getAllAuthors());

		return "authors.html";
	}
	
	@Transactional
	@GetMapping("/author/{id}")
	public String getAuthorById(@PathVariable Long id, Model model) {
		
		Author author = this.authorService.getAuthorById(id);
		if(author!=null) {
			model.addAttribute("author", author);
			model.addAttribute("photo", author.getPhotos());
			return "author.html";
		}
		
		return "error404.html";
	}
	
	// link ai libri dell'autore selezionato
	@GetMapping("/author/{id}/books")
	public String getBooksWrittenByAuthor(@PathVariable Long id, Model model) {
		Author author = this.authorService.getAuthorById(id);
		if (author == null) {
			return "error404.html";
		}
		
		model.addAttribute("author", author);
		model.addAttribute("books", author.getBooks());
		return "authorBooks.html";
	}
	
	@GetMapping("/admin/authors/newAuthor") 
	public String newAuthor(Model model) {
		model.addAttribute("author", new Author());
		model.addAttribute("books", bookService.getAllBooks());
		return "admin/formNewAuthor.html";
	}
	
	// save the author by saving it and then redirecting to his new page I guess
	@PostMapping("/author")
	public String saveAuthor(Model model, @RequestParam("photo") MultipartFile photo, Author author, BindingResult bindingResult, 
			@RequestParam(name="bookIds", required = false ) List<Long> bookIds) {
		
		if (bookIds != null && !bookIds.isEmpty()) {
	        List<Book> selectedBooks = bookService.getAllById(bookIds);
	        author.setBooks(selectedBooks);
	        for (Book b : selectedBooks) {
	            b.getAuthors().add(author); // lato non proprietario allora devo sincronizzare 
	        }
	    }
		
		this.authorValidator.validate(author, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.authorService.saveAuthor(author); 
			if (!photo.isEmpty()) {
	            try {
	                Photo authorPhoto = new Photo();
	                authorPhoto.setData(photo.getBytes());
	                authorPhoto.setAuthor(author);
	                this.photoService.savePhoto(authorPhoto);
	                
	            } catch (IOException e) {
	                e.printStackTrace();
	                model.addAttribute("errorMessage", "Errore nel caricamento della foto");
	                return "error404.html";
	            }
	        }
			
			model.addAttribute("author", author);
			return "redirect:author/"+author.getId();
		} else {
			model.addAttribute("error", "The author already exists in the system");
			return "admin/formNewAuthor.html"; 
		}
	}
	
	// controller for editing the author
	@GetMapping("/admin/author/{id}/edit")
	public String editAuthor(Model model, @PathVariable Long id) {
		Author author = this.authorService.getAuthorById(id);
		model.addAttribute("author", author);
		return "admin/editAuthor.html";
	}
	
	@PostMapping("/admin/editedAuthor") 
	public String editedAuthor(Model model, Author author) {
		this.authorService.saveAuthor(author);
		return "redirect:/author/" + author.getId();
	}
	
	// when i delete something i need to see if there are foreign keys
	@Transactional
	@GetMapping("/admin/deleteAuthor/{id}") 
	public String deleteAuthor(Model model, @PathVariable Long id) {
		Author author = this.authorService.getAuthorById(id);
		for (Book book : author.getBooks()) {
            book.getAuthors().remove(author);
            this.bookService.saveBook(book);
        }
		this.authorService.deleteAuthor(author);
		return "redirect:/authors";
	}
	
	
}
