package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;



@Entity
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String title;
	
	@Min(1)
	@Max(5)
	private Integer vote; // da 1 a 5
	
	private String text; 
	
	@ManyToOne
	private Book reviewedBook;
	
	@ManyToOne
	private SiwUser reviewedByUser;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getVote() {
		return vote;
	}

	public void setVote(Integer vote) {
		this.vote = vote;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Book getReviewedBook() {
		return reviewedBook;
	}

	public void setReviewedBook(Book reviewedBook) {
		this.reviewedBook = reviewedBook;
	}

	public SiwUser getReviewedByUser() {
		return reviewedByUser;
	}

	public void setReviewedByUser(SiwUser reviewedByUser) {
		this.reviewedByUser = reviewedByUser;
	}
}
