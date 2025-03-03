package com.booknetwork.api.book;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booknetwork.api.common.PageResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("books")
@Tag(name = "Book")
public class BookController {

    private final BookService bookService;

    @PostMapping(value = { "saveBook", "saveBook/" })
    public ResponseEntity<Long> saveBook(@RequestBody @Valid BookRequest bookRequest, Authentication loggedInUser) {

        return ResponseEntity.ok(this.bookService.saveBook(bookRequest, loggedInUser));
    }

    @GetMapping(value = { "book/{bookId}", "book/{bookId}/" })
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long bookId) {
        return ResponseEntity.ok(this.bookService.getBookById(bookId));
    }

    @GetMapping(value = { "/all", "/all/" })
    public ResponseEntity<PageResponse<BookResponse>> getAllBooks(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size, Authentication loggedInUser) {
        return ResponseEntity.ok(this.bookService.getAllBooks(page, size, loggedInUser));
    }

    @GetMapping(value = { "/owner", "/owner/" })
    public ResponseEntity<PageResponse<BookResponse>> getAllBooksByOwner(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size, Authentication loggedInUser) {
        return ResponseEntity.ok(this.bookService.getAllBooksByOwner(page, size, loggedInUser));
    }

    @GetMapping(value = { "/author/{authorName}", "/owner/{authorName}/" })
    public ResponseEntity<PageResponse<BookResponse>> getAllBooksByAuthor(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @PathVariable String authorName) {
        return ResponseEntity.ok(this.bookService.getAllBooksByAuthor(page, size, authorName));
    }

    @GetMapping(value = { "/borrowedBooks", "/borrowedBooks/" })
    public ResponseEntity<PageResponse<BorrowedBooksResponse>> getAllBorrowedBooks(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            Authentication loggedInUser) {
        return ResponseEntity.ok(this.bookService.getAllBorrowedBooks(page, size, loggedInUser));
    }

    @GetMapping(value = { "/returnedBooks", "/returnedBooks/" })
    public ResponseEntity<PageResponse<BorrowedBooksResponse>> getAllReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication loggedInUser) {
        return ResponseEntity.ok(this.bookService.getAllBorrowedBooks(page, size, loggedInUser));
    }

    @PatchMapping(value = { "/shareable/{bookId}", "/shareable/{bookId}/" })
    public ResponseEntity<Long> changeShareableStatus(
            @PathVariable Long bookId,
            Authentication loggedInUser) {
        return ResponseEntity.ok(this.bookService.updateShareableStatusOfBook(bookId, loggedInUser));
    }

    @PatchMapping(value = { "/archived/{bookId}", "/archived/{bookId}/" })
    public ResponseEntity<Long> changeArchivedStatus(
            @PathVariable Long bookId,
            Authentication loggedInUser) {
        return ResponseEntity.ok(this.bookService.updateArchivedStatusOfBook(bookId, loggedInUser));
    }

    @PostMapping(value = { "/borrow/{bookId}", "/borrow/{bookId}/" })
    public ResponseEntity<Long> borrowBook(@PathVariable Long bookId, Authentication loggedInUser) {
        return ResponseEntity.ok(this.bookService.borrowBook(bookId, loggedInUser));
    }

    @PatchMapping(value = { "/borrow/return/{bookId}", "/borrow/return/{bookId}/" })
    public ResponseEntity<Long> returnBorrowedBook(@PathVariable Long bookId, Authentication loggedInUser) {
        return ResponseEntity.ok(this.bookService.returnBorrowedBook(bookId, loggedInUser));
    }

    @PatchMapping(value = { "/borrow/return/approve/{bookId}", "/borrow/return/approve{bookId}/" })
    public ResponseEntity<Long> approveReturnOfBorrowedBook(@PathVariable Long bookId, Authentication loggedInUser) {
        return ResponseEntity.ok(this.bookService.approveReturnOfBorrowedBook(bookId, loggedInUser));
    }
}
