package com.cloud_steam.bookstore.dto;



import java.util.Set;
import java.util.UUID;


public record BookDto (
   UUID id,
   String name,
   Set<CommentDto> comments
){}
