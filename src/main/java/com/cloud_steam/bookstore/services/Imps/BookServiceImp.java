package com.cloud_steam.bookstore.services.Imps;

import com.cloud_steam.bookstore.models.Book;
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
  public Book update(Book book, UUID id) {
    Book currentBook = bookRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Book id not found"));
    currentBook.setName(book.getName());
    currentBook.setComments(book.getComments());
    return bookRepository.save(book);
  }

  @Override
  public void remove(UUID id) {
    if (bookRepository.existsById(id)) throw new IllegalArgumentException("Book id not found");
    bookRepository.deleteById(id);
  }
}
