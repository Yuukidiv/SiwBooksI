package it.uniroma3.siw.model;

import jakarta.persistence.*;

@Entity
public class Image {
    @Id
    @GeneratedValue
    private Long id;

    private String filename;

    @ManyToOne
    private Book book;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

    
}
