package ru.practicum.servlet.dto.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.model.Book;
import ru.practicum.model.Publisher;
import ru.practicum.servlet.dto.book.BookResponseDto;
import ru.practicum.servlet.dto.publisher.PublisherRequestDto;
import ru.practicum.servlet.dto.publisher.PublisherResponseDto;
import ru.practicum.servlet.dto.publisher.PublisherResponseDtoShort;

import java.util.List;

@Mapper
public interface PublisherMapper {
    Publisher fromRequestDtoToPublisher(PublisherRequestDto publisherRequestDto);

    PublisherResponseDto toPublisherResponseDto(Publisher publisher);

    PublisherResponseDtoShort toPublisherResponseDtoShort(Publisher publisher);

    List<PublisherResponseDtoShort> toPublisherResponseDtoShortList(List<Publisher> publishers);

    default BookResponseDto toBookWithPublisherDto(Book book) {
        return Mappers.getMapper(BookMapper.class).toBookResponseDto(book);
    }
}
