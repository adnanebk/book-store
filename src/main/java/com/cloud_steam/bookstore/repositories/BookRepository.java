package com.cloud_steam.bookstore.repositories;

import com.cloud_steam.bookstore.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {}
