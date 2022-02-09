package com.cloud_steam.bookstore.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CommentDto {
  private UUID id;
  private String text;
}
