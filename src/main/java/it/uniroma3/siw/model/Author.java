package it.uniroma3.siw.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String surname;
	private java.time.LocalDate dateOfBirth;
	private java.time.LocalDate dateOfDeath;
	private String nationality;
	@Column(columnDefinition = "TEXT") 
	private String description;
	
	@OneToMany(mappedBy="author", cascade = CascadeType.ALL)
	private List<Photo> photos;
	
	@ManyToMany(mappedBy = "authors")
	private List<Book> books;
	
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
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
	public java.time.LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(java.time.LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public java.time.LocalDate getDateOfDeath() {
		return dateOfDeath;
	}
	public void setDateOfDeath(java.time.LocalDate dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
