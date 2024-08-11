package com.manager.books.respositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.manager.books.entities.Book;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

	List<Book> findByTitle(String title);

	List<Book> findByAuthor(String author);

	List<Book> findByPublishYear(int value);

	List<Book> findByIsbn(String value);
}
