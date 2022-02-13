package com.cloud_steam.bookstore.dtos;



import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.UUID;


public record CommentDto(UUID id,
@NotBlank
@Length(min = 5)
String text) { }
