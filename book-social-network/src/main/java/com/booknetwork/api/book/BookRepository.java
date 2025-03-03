package com.booknetwork.api.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
	// @Query("""
	// SELECT book
	// FROM Book book
	// WHERE book.createdBy = :ownerId
	// """)
	// Page<Book> findByOwnerId(Pageable pageable, Long ownerId);
	// Page<Book> findByOwnerId(Pageable pageable, Long ownerId);

	// @Query("""
	// SELECT book
	// FROM Book book
	// WHERE book.authorName = :authorName
	// """)
	Page<Book> findByAuthorName(Pageable pageable, String authorName);

	@Query("""
			SELECT book
			FROM Book book
			WHERE book.isArchived = false
			AND book.shareable = true
			AND book.createdBy != :userId
			""")
	Page<Book> findAllDisplayableBooks(Pageable pageable, Long userId);
}
