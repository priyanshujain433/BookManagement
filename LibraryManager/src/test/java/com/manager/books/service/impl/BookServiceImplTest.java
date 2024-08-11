package com.manager.books.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.manager.books.entities.Book;
import com.manager.books.model.BookModel;
import com.manager.books.respositories.BookRepository;

@SpringBootTest
public class BookServiceImplTest {

	@Mock
	private BookRepository bookRepo;

	@InjectMocks
	private BookServiceImpl bookService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetABook() {

		Book book = new Book("101", "test1", "tester", 2024, "10101");
		when(bookRepo.findById("101")).thenReturn(Optional.of(book));

		BookModel foundBook = bookService.getABook("101");

		assertNotNull(foundBook);
		assertEquals("test1", foundBook.getTitle());
		verify(bookRepo, times(1)).findById("101");
	}

	@Test
	public void testGetAllBooks() {

		Book book1 = new Book("101", "test1", "tester", 2024, "10101");
		Book book2 = new Book("102", "test2", "tester", 2024, "10102");
		List<Book> books = Arrays.asList(book1, book2);

		Pageable pageable = PageRequest.of(0, 2, Sort.by("id"));
		Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

		when(bookRepo.findAll(pageable)).thenReturn(bookPage);

		Page<BookModel> foundBooks = bookService.getAllBooks(0, 2, "id");

		assertEquals(2, foundBooks.getTotalElements());
		assertEquals(1, foundBooks.getTotalPages());
		verify(bookRepo, times(1)).findAll(pageable);
	}

	@Test
	public void testRegisterABook() {

		Book book = new Book("101", "test1", "tester", 2024, "10101");
		when(bookRepo.save(any(Book.class))).thenReturn(book);
		BookModel bookTo = new BookModel("101", "test1", "tester", 2024, "10101");

		BookModel savedBook = bookService.registerABook(bookTo);

		assertEquals("101", savedBook.getId());
		assertEquals("test1", savedBook.getTitle());
		assertEquals(2024, savedBook.getPublishYear());

		verify(bookRepo, times(1)).save(any(Book.class));
	}

	@Test
	public void testDeleteBookData() {

		bookService.deleteBookData("101");

		verify(bookRepo, times(1)).deleteById("101");
	}
}
