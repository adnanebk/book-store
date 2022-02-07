package com.cloud_steam.bookstore.services.Imps;

import com.cloud_steam.bookstore.models.Book;
import com.cloud_steam.bookstore.models.Comment;
import com.cloud_steam.bookstore.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImpTest {

    @Mock
    private  BookRepository bookRepository;
    @InjectMocks
    private  BookServiceImp bookService;
    @Test
    void addNewBook_Success() {
        var book = new Book("prgramming in java",
                Set.of(new Comment("comment 1"),new Comment("comment 2"),new Comment("comment 3")));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        var savedBook = bookService.addNew(book);
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getComments()).hasSize(3);
    }

    @Test
    void getAllBooks_success() {
        when(bookRepository.findAll()).thenReturn(List.of(new Book()));
        var allBooks =bookService.getAll();
        assertThat(allBooks).hasSize(1);
    }

    @Test
    void getBookComments_Success() {
        UUID id = UUID.randomUUID();
        var book = new Book("a book",Set.of(new Comment()));
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        var bookComments = bookService.getBookComments(id);
        assertThat(bookComments).hasSize(1);
    }
    @Test
    void getBookComments_failed() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(()->bookService.getBookComments(id)).isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    void updateBook_Success() {
        UUID id = UUID.randomUUID();
        var book = new Book("a book",Set.of(new Comment()));
        book.setId(id);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        var updatedBook = bookService.update(book,id);
        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.getId()).isEqualTo(id);

    }
    @Test
    void updateBook_failed() {
        UUID id = UUID.randomUUID();
        var book = new Book("a book",Set.of(new Comment()));
        assertThatThrownBy(()->bookService.update(book,id)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void remove_successful() {
        UUID id = UUID.randomUUID();
        when(bookRepository.findById(id)).thenReturn(Optional.of(new Book()));
        assertThatNoException().isThrownBy(()->bookService.remove(id));
    }
    @Test
    void remove_failed() {
        UUID id = UUID.randomUUID();
        assertThatIllegalArgumentException().isThrownBy(()->bookService.remove(id));
    }
}