package com.cloud_steam.bookstore.controllers;

import com.cloud_steam.bookstore.Dto.BookDto;
import com.cloud_steam.bookstore.mappers.BookMapper;
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
 private final BookMapper bookMapper;

  @GetMapping("/{id}/comments")
  public Collection<Comment> getBookComments(@PathVariable("id") UUID id) {
    return bookService.getBookComments(id);
  }

  @PostMapping
  public ResponseEntity<Book> addBook(@RequestBody BookDto bookDto) {
    var book = bookMapper.toEntity(bookDto);
    return new ResponseEntity<>(bookService.addNew(book), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public Book updateBook(@RequestBody BookDto bookDto, @PathVariable("id") UUID id) {
    var book = bookMapper.toEntity(bookDto);
    return bookService.update(book, id);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> removeBook(@PathVariable("id") UUID id) {
    bookService.remove(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
