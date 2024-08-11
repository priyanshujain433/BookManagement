package com.manager.books.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.manager.books.model.BookModel;

public interface BookService {
	
	Page<BookModel> getAllBooks(int page, int size,String sortby);
	BookModel getABook(String id);
	BookModel registerABook(BookModel book);
	void updateBookData(BookModel book);
	void deleteBookData(String id);
	boolean bookExists(String id);
	List<BookModel> searchWith(String searchWith, String value);
	
}
