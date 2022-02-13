package com.cloud_steam.bookstore.controllers;

import com.cloud_steam.bookstore.dtos.BookDto;
import com.cloud_steam.bookstore.dtos.CommentDto;
import com.cloud_steam.bookstore.mappers.BookMapper;
import com.cloud_steam.bookstore.mappers.CommentMapper;
import com.cloud_steam.bookstore.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
  private final BookService bookService;
  private final BookMapper bookMapper;
  private final CommentMapper commentMapper;

  @GetMapping
  public Stream<BookDto> getBooks() {
    return bookService.getAll().stream().map(bookMapper::toDto);
  }

  @GetMapping("/{id}/comments")
  public Stream<CommentDto> getBookComments(@PathVariable("id") UUID id) {
    return bookService.getBookComments(id).stream().map(commentMapper::toDto);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BookDto addBook(@RequestBody @Valid Optional<BookDto> bookDto) {
    return bookDto
        .map(bookMapper::toEntity)
        .map(bookService::addNew)
        .map(bookMapper::toDto)
        .orElseThrow();
  }

  @PutMapping("/{id}")
  public BookDto updateBookBoo0k(@RequestBody @Valid Optional<BookDto> bookDto, @PathVariable("id") UUID id) {
    return bookDto
        .map(bookMapper::toEntity)
        .map(book -> bookService.update(book, id))
        .map(bookMapper::toDto)
        .orElseThrow();
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
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }
}