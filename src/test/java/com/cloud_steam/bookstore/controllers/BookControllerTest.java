package com.cloud_steam.bookstore.controllers;

import com.cloud_steam.bookstore.dtos.BookDto;
import com.cloud_steam.bookstore.mappers.BookMapper;
import com.cloud_steam.bookstore.mappers.CommentMapper;
import com.cloud_steam.bookstore.models.Book;
import com.cloud_steam.bookstore.models.Comment;
import com.cloud_steam.bookstore.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
class BookControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private BookService bookService;

  @MockBean private BookMapper bookMapper;
  @MockBean private CommentMapper commentMapper;

  @Autowired private JacksonTester<Book> jsonBook;

  private static final String ROOT = "/api/books";

  @BeforeEach
  void setUp() {
    var bookComments =
        Set.of(new Comment("comment 1"), new Comment("comment 2"), new Comment("comment 3"));
    var book1 = new Book("programming in java", bookComments);
    var book2 = new Book("getting started with docker", bookComments);
    var books = Set.of(book1, book2);
    when(bookService.getAll()).thenReturn(books);
    when(bookService.getBookComments(any(UUID.class))).thenReturn(bookComments);
    when(bookMapper.toEntity(any(BookDto.class))).thenReturn(mock(Book.class));
    when(bookMapper.toDto(any(Book.class))).thenReturn(new BookDto(UUID.randomUUID(),"name",null));
  }
  @Test
  void testGetBooks() throws Exception {
    mockMvc.perform(get(ROOT)).andExpect(status().isOk());
  }
  @Test
  void testGetBookComments() throws Exception {
    UUID id = UUID.randomUUID();
    mockMvc.perform(get(ROOT + "/" + id + "/comments")).andExpect(status().isOk());
  }

  @Test
  void testAddBook() throws Exception {
    var book = createNewBook();

    when(bookService.addNew(any(Book.class))).thenReturn(book);

    var bookAsJson = jsonBook.write(book).getJson();
        mockMvc
            .perform(post(ROOT).contentType(MediaType.APPLICATION_JSON).content(bookAsJson))
            .andExpect(status().isCreated());


  }
  @Test
  void testAddBook_failed() throws Exception {
    var book = new Book();

    when(bookService.addNew(any(Book.class))).thenReturn(book);

    var bookAsJson = jsonBook.write(book).getJson();
    mockMvc
            .perform(post(ROOT).contentType(MediaType.APPLICATION_JSON).content(bookAsJson))
            .andExpect(status().isBadRequest());
  }
  @Test
  void testUpdateBook() throws Exception {
    UUID id = UUID.randomUUID();
    var book = createNewBook();

    when(bookService.update(any(Book.class), any())).thenReturn(book);

    var bookAsJson = jsonBook.write(book).getJson();
        mockMvc
            .perform(
                put(ROOT + "/" + id, ROOT, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(bookAsJson))
            .andExpect(status().isOk());

  }

  @Test
  void testDeleteBook_success() throws Exception {
    UUID id = UUID.randomUUID();
    mockMvc.perform(delete(ROOT + "/" + id)).andExpect(status().isNoContent());
  }

  private Book createNewBook() {
    return new Book("a book", new HashSet<>());
  }
}
