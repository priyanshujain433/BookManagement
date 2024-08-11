package com.manager.books.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.manager.books.entities.Book;
import com.manager.books.model.BookModel;
import com.manager.books.respositories.BookRepository;
import com.manager.books.service.BookService;

import io.micrometer.common.util.StringUtils;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepo;
	static final String PATTERN = "^[a-zA-Z0-9_]+$";

	@Override
	public Page<BookModel> getAllBooks(int page, int size, String sortBy) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		Page<Book> books = bookRepo.findAll(pageable);
		Page<BookModel> booksRes = books.map(this::entityToDaoTransform);
		return booksRes;
	}

	@Override
	public BookModel getABook(String id) {
		if (id.matches(PATTERN)) {
			Book book = bookRepo.findById(id).orElse(null);
			BookModel bookRes = null;
			if (book != null)
				bookRes = entityToDaoTransform(book);
			return bookRes;
		}
		return null;
	}

	@Override
	public BookModel registerABook(BookModel book) {
		Book bookTo = daoToEntityTransform(book);
		bookTo = bookRepo.save(bookTo);
		book = entityToDaoTransform(bookTo);
		return book;
	}

	@Override
	public void updateBookData(BookModel book) {
		Book bookTo = daoToEntityTransform(book);
		bookRepo.save(bookTo);

	}

	@Override
	public void deleteBookData(String id) {
		bookRepo.deleteById(id);
	}

	@Override
	public boolean bookExists(String id) {
		if (StringUtils.isNotBlank(id))
			return bookRepo.existsById(id);
		return false;
	}

	private Book daoToEntityTransform(BookModel bookM) {
		return new Book(bookM.getId(), bookM.getTitle(), bookM.getAuthor(), bookM.getPublishYear(), bookM.getIsbn());
	}

	private BookModel entityToDaoTransform(Book book) {
		return new BookModel(book.getId(), book.getTitle(), book.getAuthor(), book.getPublishYear(), book.getIsbn());
	}

	@Override
	public List<BookModel> searchWith(String searchWith, String value) {
		List<BookModel> books = new ArrayList<>();
		List<Book> booksTo;
		switch (searchWith) {
		case "title" -> booksTo = bookRepo.findByTitle(value);
		case "author" -> booksTo = bookRepo.findByAuthor(value);
		case "publishYear" -> booksTo = bookRepo.findByPublishYear(Integer.valueOf(value));
		case "isbn" -> booksTo = bookRepo.findByIsbn(value);
		default -> booksTo = new ArrayList<>();
		}

		for (Book book : booksTo) {
			books.add(entityToDaoTransform(book));
		}
		return books;
	}

}
