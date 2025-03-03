package com.booknetwork.api.book;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.booknetwork.api.common.PageResponse;
import com.booknetwork.api.exception.OperationNotPermittedException;
import com.booknetwork.api.history.BookTransactionHistory;
import com.booknetwork.api.history.BookTransactionHistoryRepository;
import com.booknetwork.api.user.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final BookMapper bookMapper;
	private final BookRepository bookRepository;
	private final BookTransactionHistoryRepository bookTransactionHistoryRepository;

	@Override
	public Long saveBook(BookRequest bookRequest, Authentication loggedInUser) {
		User user = (User) loggedInUser.getPrincipal();
		Book book = bookMapper.toBook(bookRequest);
		book.setOwner(user);
		return this.bookRepository.save(book).getId();
	}

	@Override
	public BookResponse getBookById(Long id) {
		return this.bookRepository.findById(id).map(bookMapper::toBookResponse)
				.orElseThrow(() -> new EntityNotFoundException("Book not found with id:: " + id));
	}

	@Override
	public PageResponse<BookResponse> getAllBooks(int page, int size, Authentication loggedInUser) {
		User user = (User) loggedInUser.getPrincipal();
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Page<Book> books = this.bookRepository.findAllDisplayableBooks(pageable, user.getId());
		List<BookResponse> booksResponse = books.stream().map(bookMapper::toBookResponse).toList();

		return new PageResponse<>(booksResponse, books.getNumber(), books.getSize(), books.getTotalElements(),
				books.getTotalPages(), books.isFirst(), books.isLast());
	}

	@Override
	public PageResponse<BookResponse> getAllBooksByOwner(int page, int size, Authentication loggedInUser) {

		User user = (User) loggedInUser.getPrincipal();
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Page<Book> books = this.bookRepository.findAll(BookSpecification.withOwnerId(user.getId()),
				pageable);
		List<BookResponse> booksResponse = books.stream().map(bookMapper::toBookResponse).toList();
		return new PageResponse<>(booksResponse, books.getNumber(), books.getSize(), books.getTotalElements(),
				books.getTotalPages(), books.isFirst(), books.isLast());
	}

	@Override
	public PageResponse<BookResponse> getAllBooksByAuthor(int page, int size, String authorName) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Page<Book> books = this.bookRepository.findAll(BookSpecification.withAuthorName(authorName),
				pageable);
		List<BookResponse> booksResponse = books.stream().map(bookMapper::toBookResponse).toList();
		return new PageResponse<>(booksResponse, books.getNumber(), books.getSize(), books.getTotalElements(),
				books.getTotalPages(), books.isFirst(), books.isLast());
	}

	@Override
	public PageResponse<BorrowedBooksResponse> getAllBorrowedBooks(int page, int size, Authentication loggedInUser) {
		User user = (User) loggedInUser.getPrincipal();
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Page<BookTransactionHistory> allBorrowedBooks = this.bookTransactionHistoryRepository
				.findAllBorrowedBooksByUser(pageable, user.getId());

		List<BorrowedBooksResponse> booksResponse = allBorrowedBooks.stream().map(bookMapper::toBorrowedBooksResponse)
				.toList();

		return new PageResponse<BorrowedBooksResponse>(booksResponse, allBorrowedBooks.getNumber(),
				allBorrowedBooks.getSize(),
				allBorrowedBooks.getTotalElements(), allBorrowedBooks.getTotalPages(),
				allBorrowedBooks.isFirst(), allBorrowedBooks.isLast());
	}

	@Override
	public PageResponse<BorrowedBooksResponse> getAllReturnedBooks(int page, int size, Authentication loggedInUser) {
		User user = (User) loggedInUser.getPrincipal();
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Page<BookTransactionHistory> allReturnedBooks = this.bookTransactionHistoryRepository
				.findAllReturnedBooksByUser(pageable, user.getId());

		List<BorrowedBooksResponse> booksResponse = allReturnedBooks.stream().map(bookMapper::toBorrowedBooksResponse)
				.toList();

		return new PageResponse<BorrowedBooksResponse>(booksResponse, allReturnedBooks.getNumber(),
				allReturnedBooks.getSize(),
				allReturnedBooks.getTotalElements(), allReturnedBooks.getTotalPages(),
				allReturnedBooks.isFirst(), allReturnedBooks.isLast());
	}

	@Override
	public BookResponse updateBook(BookRequest request) {
		throw new UnsupportedOperationException("Unimplemented method 'updateBook'");
	}

	@Override
	public Long updateShareableStatusOfBook(Long bookId, Authentication loggedInUser) {
		User user = (User) loggedInUser.getPrincipal();
		Book book = this.bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("Book not found with id:: " + bookId));

		if (!Objects.equals(book.getOwner().getId(), user.getId())) {
			throw new OperationNotPermittedException("Only book owner can update book.");
		}
		book.setShareable(!book.isShareable());
		this.bookRepository.save(book);
		return bookId;
	}

	@Override
	public Long updateArchivedStatusOfBook(Long bookId, Authentication loggedInUser) {
		User user = (User) loggedInUser.getPrincipal();
		Book book = this.bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("Book not found with id:: " + bookId));

		if (!Objects.equals(book.getOwner().getId(), user.getId())) {
			throw new OperationNotPermittedException("Only book owner can update book.");
		}
		book.setArchived(!book.isArchived());
		this.bookRepository.save(book);
		return bookId;
	}

	@Override
	public Long borrowBook(Long bookId, Authentication loggedInUser) {
		User user = (User) loggedInUser.getPrincipal();
		Book book = this.bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("Book not found with id:: " + bookId));

		if (book.isArchived() || !book.isShareable()) {
			throw new OperationNotPermittedException("The requested book can not be borrowed as it is not shareable.");
		}

		if (Objects.equals(book.getOwner().getId(), user.getId())) {
			throw new OperationNotPermittedException("You can not borrow your own book.");
		}

		if (this.bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId())) {
			throw new OperationNotPermittedException("This book is already borrowed.");
		}

		BookTransactionHistory bth = BookTransactionHistory.builder()
				.book(book)
				.user(user)
				.returned(false)
				.returnApproved(false)
				.build();

		book.setShareable(false);
		this.bookRepository.save(book);
		return this.bookTransactionHistoryRepository.save(bth).getId();
	}

	@Override
	public Long returnBorrowedBook(Long bookId, Authentication loggedInUser) {
		User user = (User) loggedInUser.getPrincipal();
		Book book = this.bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("Book not found with id:: " + bookId));

		if (book.isArchived() || book.isShareable()) {
			throw new OperationNotPermittedException("The requested book can not be returned as it is not borrowed.");
		}

		if (Objects.equals(book.getOwner().getId(), user.getId())) {
			throw new OperationNotPermittedException("You can not borrow or return your own book.");
		}

		BookTransactionHistory bth = this.bookTransactionHistoryRepository.findByBookIdAndUserId(bookId, user.getId())
				.orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book."));

		bth.setReturned(true);
		return this.bookTransactionHistoryRepository.save(bth).getId();
	}

	@Override
	public Long approveReturnOfBorrowedBook(Long bookId, Authentication loggedInUser) {
		User user = (User) loggedInUser.getPrincipal();
		Book book = this.bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("Book not found with id:: " + bookId));

		if (book.isArchived() || book.isShareable()) {
			throw new OperationNotPermittedException(
					"The return for the requested book can not be approved as it was not borrowed.");
		}

		if (!Objects.equals(book.getOwner().getId(), user.getId())) {
			throw new OperationNotPermittedException("You do not own this book.");
		}

		BookTransactionHistory bth = this.bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId, user.getId())
				.orElseThrow(() -> new OperationNotPermittedException(
						"This book is not returned yet. So you can not approve return for this book."));

		bth.setReturnApproved(true);
		book.setShareable(true);
		this.bookRepository.save(book);
		return this.bookTransactionHistoryRepository.save(bth).getId();
	}

	@Override
	public String deleteBook(Long id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'deleteBook'");
	}

}
