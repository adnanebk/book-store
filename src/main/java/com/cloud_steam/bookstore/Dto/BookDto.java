package com.cloud_steam.bookstore.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class BookDto {
  private UUID id;
  private String name;
  private Set<CommentDto> comments;
}
