package com.cloud_steam.bookstore.repositories;

import com.cloud_steam.bookstore.models.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {

        bookRepository.saveAll(Set.of(
                new Book("book1",new HashSet<>()),
                new Book("book2",new HashSet<>())
        ));
    }
    @Test
    void saveBook_success() {
        Book book = addNewBook();
        assertThat(book).isNotNull();
        assertThat(book).hasFieldOrPropertyWithValue("name","book3");
    }
    @Test
    void findAll_success() {
        var books= bookRepository.findAll();
        assertThat(books).hasSize(2);
    }
    @Test
    void findById_success() {
        Book savedBook = addNewBook();
        var book= bookRepository.findById(savedBook.getId());
        assertThat(book).isNotEmpty();
    }


    @Test
    void removeById_success() {
        var book= addNewBook();
        bookRepository.deleteById(book.getId());
        assertThat(bookRepository.findAll()).hasSize(2);
    }
    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }
    private Book addNewBook() {
        return bookRepository.save(new Book("book3",new HashSet<>()));
    }

}