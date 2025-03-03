package com.booknetwork.api.history;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Long> {

        @Query("""
                        SELECT bth
                        FROM BookTransactionHistory bth
                        WHERE bth.user.id = :userId
                        """)
        Page<BookTransactionHistory> findAllBorrowedBooksByUser(Pageable pageable, Long userId);

        @Query("""
                        SELECT bth
                        FROM BookTransactionHistory bth
                        WHERE bth.book.owner.id = :userId
                        """)
        Page<BookTransactionHistory> findAllReturnedBooksByUser(Pageable pageable, Long userId);

        @Query("""
                        SELECT bth
                        (COUNT(*) > 0) AS isBorrowed
                        FROM BookTransactionHistory bth
                        WHERE bth.user.id = :userId
                        AND bth.book.id = :bookId
                        AND bth.returnApproved=false
                        """)
        boolean isAlreadyBorrowedByUser(Long bookId, Long userId);

        @Query("""
                        SELECT bth
                        FROM BookTransactionHistory bth
                        WHERE bth.user.id = :userId
                        AND bth.book.id = :bookId
                        AND bth.returned = false
                        AND bth.returnApproved=false
                        """)
        Optional<BookTransactionHistory> findByBookIdAndUserId(Long bookId, Long userId);

        @Query("""
                        SELECT bth
                        FROM BookTransactionHistory bth
                        WHERE bth.book.owner.id = :userId
                        AND bth.book.id = :bookId
                        AND bth.returned = true
                        AND bth.returnApproved=false
                        """)
        Optional<BookTransactionHistory> findByBookIdAndOwnerId(Long bookId, Long userId);

}
