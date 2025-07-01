package it.uniroma3.siw.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
			model.addAttribute("photo", author.getPhoto());
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
	
	// save the author by saving it and then redirecting to his new page I gues
	
	@PostMapping("/author")
	public String saveAuthor(Model model,
	                         @RequestParam("photoFile") MultipartFile photoFile,
	                         @ModelAttribute Author author,
	                         BindingResult bindingResult,
	                         @RequestParam(name = "bookIds", required = false) List<Long> bookIds) {

	    // Validazione
		System.out.println(">>> Errori nel BindingResult:");
		bindingResult.getAllErrors().forEach(err -> System.out.println(">>> " + err));
	    authorValidator.validate(author, bindingResult);
	    if (bindingResult.hasErrors()) {
	    	model.addAttribute("books", bookService.getAllBooks());
	        model.addAttribute("error", "Errore nella validazione autore");
	        return "admin/formNewAuthor.html";
	    }

	    // Libri associati (se presenti)
	    if (bookIds != null && !bookIds.isEmpty()) {
	        List<Book> selectedBooks = bookService.getAllById(bookIds);
	        author.setBooks(selectedBooks);
	        for (Book b : selectedBooks) {
	            b.getAuthors().add(author);
	        }
	    }

	    // Salvo l'autore
	    authorService.saveAuthor(author); // serve prima per settare id per FK

	    // Foto (se presente)
	    if (!photoFile.isEmpty()) {
	        try {
	            Photo authorPhoto = new Photo();
	            authorPhoto.setData(photoFile.getBytes());
	            authorPhoto.setAuthor(author);      // relaziono autore
	            author.setPhoto(authorPhoto);       // relaziono foto
	            photoService.savePhoto(authorPhoto);
	        } catch (IOException e) {
	            model.addAttribute("errorMessage", "Errore nel caricamento della foto");
	            return "error404.html";
	        }
	    }

	    model.addAttribute("author", author);
	    return "redirect:author/" + author.getId();
	}

	// controller for editing the author
	@GetMapping("/admin/author/{id}/edit")
	public String editAuthor(Model model, @PathVariable Long id) {
		Author author = this.authorService.getAuthorById(id);
		model.addAttribute("author", author);
		return "admin/editAuthor.html";
	}
	@Transactional
	@PostMapping("/admin/editedAuthor")
	public String editedAuthor(@RequestParam("id") Long id,
	                           @RequestParam("name") String name,
	                           @RequestParam("surname") String surname,
	                           @RequestParam("dateOfBirth") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
	                           @RequestParam(name = "dateOfDeath", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfDeath,
	                           @RequestParam("nationality") String nationality,
	                           @RequestParam("description") String description,
	                           @RequestParam(name = "photoFile", required = false) MultipartFile photoFile) throws IOException {

	    Author author = authorService.getAuthorById(id);

	    author.setName(name);
	    author.setSurname(surname);
	    author.setDateOfBirth(dateOfBirth);
	    author.setDateOfDeath(dateOfDeath);
	    author.setNationality(nationality);
	    author.setDescription(description);

	    if (photoFile != null && !photoFile.isEmpty()) {
	        // elimina vecchia foto se esiste
	        Photo oldPhoto = author.getPhoto();
	        if (oldPhoto != null) {
	            author.setPhoto(null); // scollega
	            photoService.deletePhoto(oldPhoto);
	        }

	        // salva nuova foto
	        Photo newPhoto = new Photo();
	        newPhoto.setData(photoFile.getBytes());
	        newPhoto.setAuthor(author);
	        photoService.savePhoto(newPhoto);

	        author.setPhoto(newPhoto);
	    }

	    authorService.saveAuthor(author);
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
	
	
	
	@GetMapping("/authors/search")
	public String searchAuthors(
	        @RequestParam(required = false) String name,
	        @RequestParam(required = false) String surname,
	        Model model) {
	    
	    List<Author> authors = authorService.searchAuthors(name, surname);
	    model.addAttribute("authors", authors);
	    model.addAttribute("name", name);
	    model.addAttribute("surname", surname);
	    
	    return "authors.html"; 
	}
	
	
	
}
