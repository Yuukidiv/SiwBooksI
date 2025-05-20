package it.uniroma3.siw.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String title;
	private Integer dateOfPublication;
	
	@ManyToMany
	private List<Author> authors;
	
	@OneToMany(mappedBy = "reviewedBook")
	private List<Review> reviews;
	
	
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

	public Integer getDateOfPublication() {
		return dateOfPublication;
	}

	public void setDateOfPublication(Integer dateOfPublication) {
		this.dateOfPublication = dateOfPublication;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
}
