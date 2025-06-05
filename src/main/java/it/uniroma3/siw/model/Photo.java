package it.uniroma3.siw.model;

import jakarta.persistence.*;

@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String fileName;

    private String contentType; // es. "image/jpeg"

    @Lob
    @Column(name = "image_data", columnDefinition = "BYTEA") // PostgreSQL
    private byte[] imageData;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = true) // Aggiungi esplicitamente
    private Author author;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = true) // Anche questo
    private Book book;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

    // Getters e setters
    
}