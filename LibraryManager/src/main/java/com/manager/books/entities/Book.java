package com.manager.books.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "books")
public class Book {

	@Id
	private String id;
	@Field("book_title")
	private String title;
	@Field("book_author")
	private String author;
	@Field("published_In")
	private int publishYear;
	@Field("isbn")
	private String isbn;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getPublishYear() {
		return publishYear;
	}

	public void setPublishYear(int publishYear) {
		this.publishYear = publishYear;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Book(String id, String title, String author, int publishYear, String isbn) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.publishYear = publishYear;
		this.isbn = isbn;
	}

	public Book() {
		// Auto-generated constructor stub
	}

}
