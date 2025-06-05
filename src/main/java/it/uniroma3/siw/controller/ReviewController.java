package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ReviewService;

@Controller
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private BookService bookService;
	@Autowired
	private CredentialsService credentialsService;
	
	
	@GetMapping("/book/addReview/{id}")
	public String writeReview(Model model, @PathVariable Long id) {
		model.addAttribute("review", new Review());
		model.addAttribute("book", this.bookService.getBookById(id));
		return "review.html";
	}
	
	
	// called by review.html need to also pass the user I guess
	@PostMapping("/review")
	public String addReview(Model model, Review review, @RequestParam("bookId") Long bookId) {
		// mi serve l'utente per calcolarlo oppure dovrei passarlo in qualche modo
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    UserDetails userDetails = (UserDetails) auth.getPrincipal();
	    Credentials cred = credentialsService.getCredentials(userDetails.getUsername());
	    User user = cred.getUser();
	    Book book = this.bookService.getBookById(bookId);
	    // se l'utente ha gia recensito questo libro allora non lo fa recensire 2 volte
	    boolean alreadyReviewed = reviewService.hasUserAlreadyReviewedBook(user, book);
	    if (alreadyReviewed) {
	        model.addAttribute("errorMessage", "Hai già recensito questo libro.");
	        model.addAttribute("book", book);
	        return "review.html"; 
	    }
		
		review.setReviewedBook(book);
		review.setReviewedByUser(user);
		
		
		this.reviewService.save(review);
		return "redirect:/book/" + bookId;
	}
	

	
	@PostMapping("/admin/delete/review/{id}")
	public String deleteReview(Model model, @PathVariable("id") Long id, @RequestParam("bookId") Long bookId) {
		Review r = this.reviewService.getReviewById(id);
		this.reviewService.delete(r);
		
		return "redirect:/book/" + bookId;
		
	}
	

}
