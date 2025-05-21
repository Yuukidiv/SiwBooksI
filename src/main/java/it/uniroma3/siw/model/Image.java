package it.uniroma3.siw.model;

import jakarta.persistence.*;

@Entity
public class Image {
    @Id
    @GeneratedValue
    private Long id;

    private String filename;	// name of the file in the fylesystem
    
    private String name;
    private String type;
    

    @ManyToOne
    private Book book;	// the book has many photos rn future also for the authors and the user/admin

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    
}
