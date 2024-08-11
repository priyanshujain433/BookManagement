package com.manager.books.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manager.books.model.BookModel;
import com.manager.books.service.BookService;

import io.micrometer.common.util.StringUtils;

@RestController
public class LibraryController {

	@Autowired
	private BookService bookService;

	@PostMapping("/api/books")
	public ResponseEntity registerBook(@RequestBody BookModel book) {
		if (bookService.bookExists(book.getId())) {
			return new ResponseEntity<>("Book with reference id:" + book.getId() + " already exists",
					HttpStatus.NOT_ACCEPTABLE);
		}
		if (book != null && validateDetails(book)) {
			book = bookService.registerABook(book);
			return new ResponseEntity<>(book, HttpStatus.OK);
		}
		return new ResponseEntity<>("Invalid Input", HttpStatus.NOT_ACCEPTABLE);

	}

	@PutMapping("/api/books/{id}")
	public ResponseEntity updateBook(@PathVariable("id") String id, @RequestBody BookModel book) {
		if (!bookService.bookExists(id)) {
			return new ResponseEntity<>("Book with reference id :" + id + " does not exists", HttpStatus.BAD_REQUEST);
		}
		if (StringUtils.isNotEmpty(id) && book != null && validateDetails(book)) {
			bookService.updateBookData(book);
			return new ResponseEntity<>("Reference Id:" + id + "with Input successfully updated", HttpStatus.OK);
		}
		return new ResponseEntity<>("Invalid Input", HttpStatus.NOT_ACCEPTABLE);
	}

	@GetMapping("/api/books/{id}")
	public ResponseEntity retrieveBook(@PathVariable("id") String id) {
		if (StringUtils.isNotEmpty(id)) {
			BookModel book = bookService.getABook(id);
			if (Optional.ofNullable(book).isEmpty()) {
				return new ResponseEntity<>("Book with reference id:" + id + " not found", HttpStatus.OK);
			}
			return new ResponseEntity<>(book, HttpStatus.OK);
		}
		return new ResponseEntity<>("Book reference id can not be empty", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/api/books")
	public ResponseEntity retrieveAllBooks(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy) {
		System.out.println("Input");
		Page<BookModel> books = bookService.getAllBooks(page, size, sortBy);
		if (books.isEmpty()) {
			return new ResponseEntity("No Books Available", HttpStatus.OK);
		}
		return new ResponseEntity(books, HttpStatus.OK);
	}

	@DeleteMapping("/api/books/{id}")
	public ResponseEntity<String> deleteABook(@PathVariable("id") String id) {
		if (StringUtils.isNotBlank(id)) {
			if (bookService.bookExists(id)) {
				bookService.deleteBookData(id);
				return new ResponseEntity<>("Book with " + id + " reference id has been deleted successfully",
						HttpStatus.OK);
			}
		}
		return new ResponseEntity<>("Book with " + id + " reference id does not exists", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/api/books/search")
	public ResponseEntity retrieveWith(@RequestParam(name = "searchWith", defaultValue = "id") String searchWith,
			@RequestParam(name = "value") String value) {
		if (StringUtils.isNotBlank(searchWith) && StringUtils.isNotBlank(value)) {
			if ((searchWith.equals("publishYear")) && (value.matches("^[a-zA-Z]+$"))) {
				return new ResponseEntity<>("Invalid value passed for publish Year", HttpStatus.BAD_REQUEST);
			}
			List<BookModel> books = bookService.searchWith(searchWith, value);
			return new ResponseEntity<>(books, HttpStatus.OK);
		}
		return new ResponseEntity<>("Invalid Request", HttpStatus.BAD_REQUEST);
	}

	private boolean validateDetails(BookModel book) {
		String alphaNeumeric = "^[a-zA-Z0-9_]+$";
		String alphaNeumericWithSpace = "^[a-zA-Z0-9_ ]+$";
		String alpha = "^[a-zA-Z ]+$";
		if (StringUtils.isBlank(book.getId()) || !book.getId().matches(alphaNeumeric)) {
			return false;
		}
		if (StringUtils.isBlank(book.getTitle()) || !book.getTitle().matches(alphaNeumericWithSpace)) {
			return false;
		}
		if (StringUtils.isBlank(book.getAuthor()) || !book.getAuthor().matches(alpha)) {
			return false;
		}
		if (book.getPublishYear() < 0 || String.valueOf(book.getPublishYear()).length() != 4) {
			return false;
		}
		return true;
	}
}
