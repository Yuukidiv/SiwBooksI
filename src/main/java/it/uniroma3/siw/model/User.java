package it.uniroma3.siw.model;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
	@Id 
	@GeneratedValue
	private Long id;

	private String name;
	private String surname;
	private String email;
	
	@OneToMany(mappedBy = "reviewedByUser")
	private List<Review> writtenReviews;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Photo photo;
	
	
	public List<Review> getWrittenReviews() {
		return writtenReviews;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public void setWrittenReviews(List<Review> writtenReviews) {
		this.writtenReviews = writtenReviews;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
