package com.booknetwork.api.book;

import org.springframework.stereotype.Service;

import com.booknetwork.api.history.BookTransactionHistory;

@Service
public class BookMapper {

    public Book toBook(BookRequest bookRequest) {
        return Book
                .builder()
                .id(bookRequest.id())
                .title(bookRequest.title())
                .authorName(bookRequest.authorName())
                .publicationYear(bookRequest.publicationYear())
                .publisher(bookRequest.publisher())
                .isbn(bookRequest.isbn())
                .synopsis(bookRequest.synopsis())
                .shareable(bookRequest.shareable())
                .isArchived(false)
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .publicationYear(book.getPublicationYear())
                .publisher(book.getPublisher())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .shareable(book.isShareable())
                .isArchived(book.isArchived())
                .ratings(book.getRatings())
                .owner(book.getOwner().getFullName())
                // .coverImage(null)
                .build();
    }

    public BorrowedBooksResponse toBorrowedBooksResponse(BookTransactionHistory history) {
        return BorrowedBooksResponse.builder()
                .id(history.getBook().getId())
                .title(history.getBook().getTitle())
                .authorName(history.getBook().getAuthorName())
                .isbn(history.getBook().getIsbn())
                .ratings(history.getBook().getRatings())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}
