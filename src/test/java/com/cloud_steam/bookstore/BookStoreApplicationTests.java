package com.cloud_steam.bookstore;

import com.cloud_steam.bookstore.dtos.BookDto;
import com.cloud_steam.bookstore.models.Book;
import com.cloud_steam.bookstore.models.Comment;
import com.cloud_steam.bookstore.repositories.CommentRepository;
import com.cloud_steam.bookstore.services.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BookStoreApplicationTests {
  @Autowired TestRestTemplate testRestTemplate;
  @Autowired private BookService bookService;
  @Autowired private CommentRepository commentRepository;
  private static final String ROOT = "/api/books";


  @Test
  void testGetBooks() throws Exception {
    var book = new Book("programming in java", Set.of(new Comment("a comment")));
    bookService.addNew(book);

    var response = testRestTemplate.getForEntity(ROOT, BookDto[].class);

    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertThat(response.getBody()).isNotEmpty();
  }

  @Test
  void testGetBookComments() throws Exception {
    var book = new Book("programming in java", Set.of(new Comment("a comment")));
    book = bookService.addNew(book);
    var url = ROOT + "/" + book.getId() + "/comments";

    var response = testRestTemplate.getForEntity(url,  BookDto[].class);

    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertThat(response.getBody()).isNotEmpty();
  }
  @Test
  void testAddBook() throws Exception {
    var book = new Book("a book", new HashSet<>());

    var bookDto = testRestTemplate.postForEntity(ROOT, book, BookDto.class);

    assertTrue(bookDto.getStatusCode().is2xxSuccessful());
    assertThat(bookDto.getBody()).isNotNull();
    assertThat(bookDto.getBody().name()).isEqualTo("a book");
  }


  @Test
  void testUpdateBook() throws Exception {
    var book = new Book("a book", null);
    book = bookService.addNew(book);
    book.setName("new name");

    var response =
        testRestTemplate.exchange(
            ROOT + "/" + book.getId(), HttpMethod.PUT, new HttpEntity<>(book), BookDto.class);

    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().name()).isEqualTo("new name");
  }

  @Test
  void testDeleteBook_success() throws Exception {
    var book = new Book("a book", Set.of(new Comment("a comment bla bla")));
    book = bookService.addNew(book);

    var response =
        testRestTemplate.exchange(ROOT + "/" + book.getId(), HttpMethod.DELETE, null, Void.class);

    assertTrue(response.getStatusCode().is2xxSuccessful());
    // test cascade remove
    assertThat(commentRepository.findById(book.getComments().stream().findFirst().get().getId()))
        .isEmpty();
  }
}
