package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ReviewRepository;

@Service
public class ReviewService {
	@Autowired
	private ReviewRepository reviewRepository;
	
	public List<Review> getAllReviews() {
		return (List<Review>) reviewRepository.findAll();
	}
	
	public void save(Review review) {
		this.reviewRepository.save(review);
	}
}
