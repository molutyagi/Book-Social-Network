package com.booknetwork.api.book;

import java.util.List;

import com.booknetwork.api.common.BaseEntity;
import com.booknetwork.api.feedback.Feedback;
import com.booknetwork.api.history.BookTransactionHistory;
import com.booknetwork.api.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Book extends BaseEntity {

    private String title;
    private String authorName;
    private String publicationYear;
    private String publisher;
    private String isbn;
    private String synopsis;
    private String bookCover;

    private boolean isArchived;
    private boolean shareable;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> bookTransactions;

}
