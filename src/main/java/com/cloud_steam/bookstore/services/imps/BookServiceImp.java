package com.cloud_steam.bookstore.services.imps;

import com.cloud_steam.bookstore.exceptions.BookNotFoundException;
import com.cloud_steam.bookstore.models.Book;
import com.cloud_steam.bookstore.models.Comment;
import com.cloud_steam.bookstore.repositories.BookRepository;
import com.cloud_steam.bookstore.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImp implements BookService {

  private final BookRepository bookRepository;

  @Override
  public Book addNew(Book book) {
    return bookRepository.save(book);
  }

  @Override
  public Collection<Book> getAll() {
    return bookRepository.findAll();
  }

  @Override
  public Collection<Comment> getBookComments(UUID id) {
    return findBookById(id).getComments();
  }

  @Override
  public Book update(Book book, UUID id) {
    Book currentBook = findBookById(id);
    currentBook.setName(book.getName());
    currentBook.setComments(book.getComments());
    return bookRepository.save(book);
  }

  @Override
  public void remove(UUID id) {
    bookRepository.deleteById(id);
  }

  private Book findBookById(UUID id) {
    return bookRepository
        .findById(id)
        .orElseThrow(() -> new BookNotFoundException("Book id not found"));
  }
}
