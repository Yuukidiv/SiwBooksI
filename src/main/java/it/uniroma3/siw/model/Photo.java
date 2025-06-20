package it.uniroma3.siw.model;

import jakarta.persistence.*;

@Entity
public class Photo {
	
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Basic(optional = false)
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "book_id") 
    private Book book;
    
    @ManyToOne
    @JoinColumn(name = "user_id") 
    private User user;

	public Long getId() {
		return id;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void setId(Long id) {
		this.id = id;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	

}