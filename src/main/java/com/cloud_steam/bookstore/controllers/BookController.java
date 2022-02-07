package com.cloud_steam.bookstore.controllers;

import com.cloud_steam.bookstore.models.Book;
import com.cloud_steam.bookstore.models.Comment;
import com.cloud_steam.bookstore.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
  private final BookService bookService;

  @GetMapping("/${id}")
  public Collection<Comment> getBookComments(@PathVariable("id") UUID id) {
    return bookService.getBookComments(id);
  }

  @PostMapping
  public ResponseEntity<Book> addBook(@RequestBody Book book) {
    return new ResponseEntity<>(bookService.addNew(book), HttpStatus.CREATED);
  }

  @PutMapping("/${id}")
  public Book updateBook(@RequestBody Book book, @PathVariable("id") UUID id) {
    return bookService.update(book, id);
  }

  @DeleteMapping("/${id}")
  public ResponseEntity<HttpStatus> updateBook(@PathVariable("id") UUID id) {
    bookService.remove(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
