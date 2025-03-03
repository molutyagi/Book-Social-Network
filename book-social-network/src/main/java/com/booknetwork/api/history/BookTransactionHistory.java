package com.booknetwork.api.history;

import com.booknetwork.api.book.Book;
import com.booknetwork.api.common.BaseEntity;
import com.booknetwork.api.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class BookTransactionHistory extends BaseEntity {

    // user relationships
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // book relationships
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private boolean returned;
    private boolean returnApproved;

}
