package com.cloud_steam.bookstore.controllers;

import com.cloud_steam.bookstore.dtos.BookDto;
import com.cloud_steam.bookstore.mappers.BookMapper;
import com.cloud_steam.bookstore.models.Book;
import com.cloud_steam.bookstore.models.Comment;
import com.cloud_steam.bookstore.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;
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
  @ResponseStatus(HttpStatus.CREATED)
  public Book addBook(@RequestBody Optional<BookDto> bookDto) {
    return bookDto
        .map(bookMapper::toEntity)
        .map(bookService::addNew)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "the book is empty"));
  }

  @PutMapping("/{id}")
  public Book updateBook(@RequestBody Optional<BookDto> bookDto, @PathVariable("id") UUID id) {
    return bookDto
        .map(bookMapper::toEntity)
        .map(book -> bookService.update(book, id))
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "the book is empty"));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeBook(@PathVariable("id") UUID id) {
    bookService.remove(id);
  }
}
