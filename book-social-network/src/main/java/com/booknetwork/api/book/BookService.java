package com.booknetwork.api.book;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.booknetwork.api.common.PageResponse;
import com.booknetwork.api.user.User;

public interface BookService {

    Long saveBook(BookRequest bookRequest, Authentication loggedInUser);

    // All get methods to get the book
    BookResponse getBookById(Long id);

    List<BookResponse> getByAuthorName(String authorName);

    List<BookResponse> getByOwner(User owner);

    PageResponse<BookResponse> getAllBooks(int page, int size, Authentication loggedInUser);

    // Other methods to update, delete, etc. the book
    BookResponse updateBook(BookRequest request);

    String deleteBook(Long id);

}
