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

    @GetMapping("all/")
    public ResponseEntity<PageResponse<BookResponse>> getAllBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size, Authentication loggedInUser) {
        return ResponseEntity.ok(this.bookService.getAllBooks(page, size, loggedInUser));
    }

}
