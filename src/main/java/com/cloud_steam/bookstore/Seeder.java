package com.cloud_steam.bookstore;

import com.cloud_steam.bookstore.models.Book;
import com.cloud_steam.bookstore.models.Comment;
import com.cloud_steam.bookstore.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class Seeder {
private final BookService bookService;

    @EventListener
    public void seedData(ContextRefreshedEvent event) {
        var books = List.of(
                new Book("book 1", Set.of(new Comment("text text"))),
                new Book("book 2", Set.of(new Comment("text text2"))),
                new Book("book 3", Set.of(new Comment("text text3")))
        );
books.forEach(bookService::addNew);
    }
}
