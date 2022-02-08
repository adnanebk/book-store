package com.cloud_steam.bookstore.controllers;

import com.cloud_steam.bookstore.Dto.BookDto;
import com.cloud_steam.bookstore.mappers.BookMapper;
import com.cloud_steam.bookstore.models.Book;
import com.cloud_steam.bookstore.models.Comment;
import com.cloud_steam.bookstore.services.BookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@AutoConfigureJsonTesters
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;

    @MockBean
    private BookMapper bookMapper;

    @Autowired
    private JacksonTester<Book> jsonBook;

    private static final String ROOT= "/api/books";

    @BeforeEach
    void setUp() {
        var bookComments = Set.of(new Comment("comment 1"),new Comment("comment 2"),new Comment("comment 3"));
        var book1 = new Book("programming in java",bookComments );
        var book2 = new Book("getting started with docker",bookComments);
        var books = Set.of(book1,book2);
        when(bookService.getAll()).thenReturn(books);
        when(bookService.getBookComments(any(UUID.class))).thenReturn(bookComments);

    }

    @Test
    void testGetBookComments() throws Exception {
        UUID id = UUID.randomUUID();
        String api = createApi(id,"comments");
        mockMvc.perform(get(api)).andExpect(status().isOk());
    }

    @Test
    void testAddBook() throws Exception {
        var book = createNewBook();
        when(bookMapper.toEntity(any(BookDto.class))).thenReturn(book);

        when(bookService.addNew(any(Book.class))).thenReturn(book);
        var bookAsJson = jsonBook.write(book).getJson();
        var response=mockMvc.perform(post(ROOT).contentType(MediaType.APPLICATION_JSON).content(bookAsJson))
                .andExpect(status().isCreated()).andReturn().getResponse();
        assertThat(response.getContentAsString()).isEqualTo(bookAsJson);
    }

    @Test
    void testUpdateBook() throws Exception {
        UUID id=UUID.randomUUID();
        var book = createNewBook();
        when(bookMapper.toEntity(any(BookDto.class))).thenReturn(book);
        book.setId(id);
        var api = createApi(id);
        when(bookService.update(any(Book.class),any())).thenReturn(book);
        var bookAsJson = jsonBook.write(book).getJson();
        var response=mockMvc.perform(put(api).contentType(MediaType.APPLICATION_JSON).content(bookAsJson))
                .andExpect(status().isOk()).andReturn().getResponse();
        assertThat(response.getContentAsString()).isEqualTo(bookAsJson);
    }

    @Test
    void testDeleteBook_success() throws Exception {
        UUID id=UUID.randomUUID();
        var api = createApi(id);
        doNothing().when(bookService).remove(any());
        mockMvc.perform(delete(api)).andExpect(status().isNoContent());
    }

    private Book createNewBook() {
        return new Book("a book",new HashSet<>());
    }
    private <T> String createApi(T... paths){
        String api=ROOT;
        for (T path : paths)
            api+="/"+path;

        return  api;
    }

}