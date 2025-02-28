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
public class BookResponse {
    private Long id;
    private String title;
    private String authorName;
    private String publicationYear;
    private String publisher;
    private String isbn;
    private String synopsis;
    private String owner;
    private byte[] coverImage;
    private double ratings;
    private boolean isArchived;
    private boolean shareable;
}
