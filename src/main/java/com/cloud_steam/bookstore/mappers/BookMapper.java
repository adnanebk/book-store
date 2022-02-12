package com.cloud_steam.bookstore.mappers;

import com.cloud_steam.bookstore.dtos.BookDto;
import com.cloud_steam.bookstore.models.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
  BookDto toDto(Book book);

  Book toEntity(BookDto bookDto);
}
