package ru.practicum.servlet.dto.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.model.Author;
import ru.practicum.model.Book;
import ru.practicum.servlet.dto.author.AuthorRequestDto;
import ru.practicum.servlet.dto.author.AuthorResponseDto;
import ru.practicum.servlet.dto.author.AuthorResponseDtoShort;
import ru.practicum.servlet.dto.book.BookWithPublisherDto;

import java.util.List;

@Mapper
public interface AuthorMapper {
    AuthorResponseDto authorToResponseDto(Author author);

    Author fromAuthorRequestDto(AuthorRequestDto dto);

    AuthorResponseDtoShort authorToResponseDtoShort(Author author);

    List<AuthorResponseDtoShort> authorListToResponseDtoShortList(List<Author> authorList);

    default BookWithPublisherDto toBookWithPublisherDto(Book book) {
        return Mappers.getMapper(BookMapper.class).toBookWithPublisherDto(book);
    }
}
