package ru.practicum.servlet.dto.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.practicum.model.Author;
import ru.practicum.model.Book;
import ru.practicum.model.Publisher;
import ru.practicum.servlet.dto.book.BookDto;
import ru.practicum.servlet.dto.book.BookResponseDto;
import ru.practicum.servlet.dto.book.BookUpdateDto;
import ru.practicum.servlet.dto.book.BookWithPublisherDto;
import ru.practicum.servlet.dto.publisher.PublisherResponseDtoShort;

import java.util.List;

@Mapper
public interface BookMapper {
    BookWithPublisherDto toBookWithPublisherDto(Book book);

    BookResponseDto toBookResponseDto(Book book);

    List<BookResponseDto> toBookResponseDtoList(List<Book> books);

    @Mapping(target = "author", source = "author", qualifiedByName = "toAuthor")
    @Mapping(target = "publishers", source = "publishers", qualifiedByName = "toPublishers")
    Book fromBookDto(BookDto bookDto);

    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "publishers", source = "publishers", qualifiedByName = "tuPublisherId")
    BookDto toBookDto(Book book);

    @Mapping(target = "author", source = "author", qualifiedByName = "toAuthor")
    Book fromBookUpdateDto(BookUpdateDto dto);

    default List<PublisherResponseDtoShort> toPublisherDtoShortList(List<Publisher> publishers) {
        return Mappers.getMapper(PublisherMapper.class).toPublisherResponseDtoShortList(publishers);
    }

    @Named("toAuthor")
    default Author toAuthorFromId(long id) {
        return Author.builder().id(id).build();
    }

    @Named("toPublishers")
    default List<Publisher> toPublishersFromIds(List<Long> ids) {
        return ids.stream().map(id -> Publisher.builder().id(id).build()).toList();
    }

    @Named("tuPublisherId")
    default List<Long> toPublishersIdFromPublisher(List<Publisher> publishers) {
        return publishers.stream().map(Publisher::getId).toList();
    }
}
