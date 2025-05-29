package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.ReviewService;

@Controller
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private BookService bookService;
	
	
	@GetMapping("/book/addReview/{id}")
	public String writeReview(Model model, @PathVariable Long id) {
		model.addAttribute("review", new Review());
		model.addAttribute("book", this.bookService.getBookById(id));
		return "review.html";
	}
	
	@PostMapping("/review")
	public String addReview(Model model, Review review, @RequestParam("bookId") Long bookId) {
		Book book = this.bookService.getBookById(bookId);
		review.setReviewedBook(book);
		
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
