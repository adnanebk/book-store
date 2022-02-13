package com.cloud_steam.bookstore.controllers;

import com.cloud_steam.bookstore.dtos.BookDto;
import com.cloud_steam.bookstore.mappers.BookMapper;
import com.cloud_steam.bookstore.models.Book;
import com.cloud_steam.bookstore.models.Comment;
import com.cloud_steam.bookstore.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
  private final BookService bookService;
  private final BookMapper bookMapper;

  @GetMapping
  public Collection<Book> getBooks() {
    return bookService.getAll();
  }

  @GetMapping("/{id}/comments")
  public Collection<Comment> getBookComments(@PathVariable("id") UUID id) {
    return bookService.getBookComments(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Book addBook(@RequestBody @Valid Optional<BookDto> bookDto) {
    return bookDto
        .map(bookMapper::toEntity)
        .map(bookService::addNew)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "the book is empty"));
  }

  @PutMapping("/{id}")
  public Book updateBook(@RequestBody @Valid Optional<BookDto> bookDto, @PathVariable("id") UUID id) {
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

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(
          MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }
}