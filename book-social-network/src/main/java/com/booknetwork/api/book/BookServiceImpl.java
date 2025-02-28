package com.booknetwork.api.book;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.booknetwork.api.common.PageResponse;
import com.booknetwork.api.user.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final BookMapper bookMapper;
	private final BookRepository bookRepository;

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
	public List<BookResponse> getByAuthorName(String authorName) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getByAuthorName'");
	}

	@Override
	public List<BookResponse> getByOwner(User owner) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getByOwner'");
	}

	@Override
	public BookResponse updateBook(BookRequest request) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'updateBook'");
	}

	@Override
	public String deleteBook(Long id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'deleteBook'");
	}

}
