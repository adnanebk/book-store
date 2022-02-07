package com.cloud_steam.bookstore.repositories;

import com.cloud_steam.bookstore.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
}