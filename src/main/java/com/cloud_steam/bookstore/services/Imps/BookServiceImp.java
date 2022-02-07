package com.cloud_steam.bookstore.services.Imps;

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
    Book currentBook = getBookFromRepo(id);
    return currentBook.getComments();
  }

  @Override
  public Book update(Book book, UUID id) {
    Book currentBook = getBookFromRepo(id);
    currentBook.setName(book.getName());
    currentBook.setComments(book.getComments());
    return bookRepository.save(book);
  }

  @Override
  public void remove(UUID id) {
    if (getBookFromRepo(id)!=null)
      bookRepository.deleteById(id);
  }

  private Book getBookFromRepo(UUID id) {
    return bookRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Book id not found"));

  }
}
