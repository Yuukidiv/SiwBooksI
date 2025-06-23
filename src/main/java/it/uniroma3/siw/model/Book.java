package it.uniroma3.siw.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String title;
	private Integer dateOfPublication;
	@Column(columnDefinition = "TEXT") 
	private String description;
	
	@ManyToMany
	private List<Author> authors;
	
	@OneToMany(mappedBy = "reviewedBook", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Review> reviews;
	
	@OneToOne(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	private Photo photo;
	
	@ElementCollection(targetClass = Genre.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "book_genres", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "genre")
    private Set<Genre> genres = new HashSet<>();
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public Set<Genre> getGenres() {
		return genres;
	}

	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}


	
	
	
}
