package com.booknetwork.api.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowedBooksResponse {
    private Long id;
    private String title;
    private String authorName;
    private String isbn;
    private double ratings;
    private boolean returned;
    private boolean returnApproved;
}
