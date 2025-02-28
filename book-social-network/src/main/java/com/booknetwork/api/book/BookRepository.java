package com.booknetwork.api.book;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.booknetwork.api.user.User;

public interface BookRepository extends JpaRepository<Book, Long> {
	Optional<Book> findByAuthorName(String authorName);

	Optional<Book> findByOwner(User owner);

	@Query("""
			SELECT book
			FROM Book book
			WHERE book.isArchived = false
			AND book.shareable = true
			AND book.createdBy != :userId
			""")
	Page<Book> findAllDisplayableBooks(Pageable pageable, Long userId);
}
