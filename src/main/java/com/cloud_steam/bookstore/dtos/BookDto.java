package com.cloud_steam.bookstore.dtos;



import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.UUID;


public record BookDto (
   UUID id,
   @NotBlank String name,
   Set<CommentDto> comments
){}
