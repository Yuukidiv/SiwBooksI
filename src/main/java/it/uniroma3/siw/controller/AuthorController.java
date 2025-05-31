package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.service.AuthorService;

@Controller
public class AuthorController {

	@Autowired
	private AuthorService authorService;
	
	@GetMapping("/authors")
	public String getAllAuthors(Model model) {
		model.addAttribute("authors", this.authorService.getAllAuthors());
		return "authors.html";
	}
	
	@GetMapping("/author/{id}")
	public String getAuthorById(@PathVariable Long id, Model model) {
		model.addAttribute("author", this.authorService.getAuthorById(id));
		return "author.html";
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
		// libri da inserire per l'autore model.addAttribute("books", this)
		return "admin/formNewAuthor.html";
	}
	
	// save the author by saving it and then redirecting to his new page I guess
	@PostMapping("/author")
	public String saveAuthor(Model model, Author author) {
		System.out.println(">> ID DELLAUTORE CHE DUPLICA NON SI SA PERCHE: " + author.getId());
		this.authorService.saveAuthor(author);
		return "redirect:author/" + author.getId();
	}
}
