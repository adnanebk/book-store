package com.cloud_steam.bookstore.mappers;

import com.cloud_steam.bookstore.Dto.CommentDto;
import com.cloud_steam.bookstore.models.Book;
import com.cloud_steam.bookstore.models.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDto toDto(Comment comment);

    Comment toEntity(CommentDto bookDto);
}
