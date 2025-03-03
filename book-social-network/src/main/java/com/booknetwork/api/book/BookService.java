package com.booknetwork.api.book;

import org.springframework.security.core.Authentication;

import com.booknetwork.api.common.PageResponse;

public interface BookService {

    Long saveBook(BookRequest bookRequest, Authentication loggedInUser);

    // All get methods to get the book
    BookResponse getBookById(Long id);

    PageResponse<BookResponse> getAllBooksByAuthor(int page, int size, String authorName);

    PageResponse<BookResponse> getAllBooksByOwner(int page, int size, Authentication loggedInUser);

    PageResponse<BookResponse> getAllBooks(int page, int size, Authentication loggedInUser);

    PageResponse<BorrowedBooksResponse> getAllBorrowedBooks(int page, int size, Authentication loggedInUser);

    PageResponse<BorrowedBooksResponse> getAllReturnedBooks(int page, int size, Authentication loggedInUser);

    // Other methods to update, delete, etc. the book
    BookResponse updateBook(BookRequest request);

    Long updateShareableStatusOfBook(Long bookId, Authentication loggedInUser);

    Long updateArchivedStatusOfBook(Long bookId, Authentication loggedInUser);

    Long borrowBook(Long bookId, Authentication loggedInUser);

    Long returnBorrowedBook(Long bookId, Authentication loggedInUser);

    Long approveReturnOfBorrowedBook(Long bookId, Authentication loggedInUser);

    String deleteBook(Long id);

}
