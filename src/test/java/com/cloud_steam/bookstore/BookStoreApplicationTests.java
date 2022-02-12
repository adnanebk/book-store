package com.cloud_steam.bookstore;

import com.cloud_steam.bookstore.models.Book;
import com.cloud_steam.bookstore.models.Comment;
import com.cloud_steam.bookstore.repositories.CommentRepository;
import com.cloud_steam.bookstore.services.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class BookStoreApplicationTests {
  @Autowired private MockMvc mockMvc;
  @Autowired private BookService bookService;
  @Autowired private JacksonTester<Book> jsonBook;
  @Autowired private CommentRepository commentRepository;

  private static final String ROOT = "/api/books";

  @Test
  void testGetBookComments() throws Exception {
    var comment = commentRepository.save(new Comment("a comment"));
    var book = new Book("programming in java", Set.of(comment));
    book = bookService.addNew(book);
    mockMvc
        .perform(get(ROOT + "/" + book.getId() + "/comments"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].text", is("a comment")));
  }

  @Test
  void testAddBook() throws Exception {
    var book = new Book("a book", new HashSet<>());
    ;
    var bookAsJson = jsonBook.write(book).getJson();
    var response =
        mockMvc
            .perform(post(ROOT).contentType(MediaType.APPLICATION_JSON).content(bookAsJson))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse();

    assertThat(response.getContentAsString()).isEqualTo(bookAsJson);
  }

  @Test
  void testUpdateBook() throws Exception {
    var book = new Book("a book", new HashSet<>());
    book = bookService.addNew(book);
    book.setName("new name");
    var bookAsJson = jsonBook.write(book).getJson();
    var response =
        mockMvc
            .perform(
                put(ROOT + "/" + book.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(bookAsJson))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

    assertThat(response.getContentAsString()).isEqualTo(bookAsJson);
  }

  @Test
  void testDeleteBook_success() throws Exception {
    var book = new Book("a book", new HashSet<>());
    book = bookService.addNew(book);
    mockMvc.perform(delete(ROOT + "/" + book.getId())).andExpect(status().isNoContent());
  }
}
