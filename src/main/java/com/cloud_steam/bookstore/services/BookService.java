package com.cloud_steam.bookstore.services;

import com.cloud_steam.bookstore.models.Book;
import com.cloud_steam.bookstore.models.Comment;

import java.util.Collection;
import java.util.UUID;

public interface BookService {
    Book addNew(Book book);

    Collection<Book> getAll();

    Collection<Comment> getBookComments(UUID id);

    Book update(Book book, UUID id);

    void remove(UUID id);
}
