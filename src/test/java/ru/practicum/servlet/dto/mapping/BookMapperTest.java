package ru.practicum.servlet.dto.mapping;

import org.junit.jupiter.api.Test;
import ru.practicum.model.Author;
import ru.practicum.model.Book;
import ru.practicum.model.Publisher;
import ru.practicum.repository.dataUtils.CreatedData;
import ru.practicum.servlet.dto.book.BookDto;
import ru.practicum.servlet.dto.book.BookResponseDto;
import ru.practicum.servlet.dto.book.BookUpdateDto;
import ru.practicum.servlet.dto.book.BookWithPublisherDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookMapperTest {
    private AuthorMapper authorMapper = new AuthorMapperImpl();
    private BookMapper bookMapper = new BookMapperImpl();
    private PublisherMapper publisherMapper = new PublisherMapperImpl();
    private Author author = CreatedData.createAuthor(1);
    private Book book = CreatedData.createBook(1);
    private Publisher publisher = CreatedData.createPublisher(1);

    @Test
    void toBookWithPublisherDto() {
        //given
        BookWithPublisherDto expected = new BookWithPublisherDto();
        expected.setId(book.getId());
        expected.setName(book.getName());
        expected.setYear(book.getYear());
        expected.setAuthor(authorMapper.authorToResponseDtoShort(author));
        book.addPublisher(publisher);
        book.setAuthor(author);
        expected.setPublishers(publisherMapper.toPublisherResponseDtoShortList(List.of(publisher)));
        //when
        BookWithPublisherDto actual = bookMapper.toBookWithPublisherDto(book);
        BookWithPublisherDto actualNull = bookMapper.toBookWithPublisherDto(null);
        //that
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
        assertThat(actualNull).isNull();
    }

    @Test
    void toBookResponseDto() {
        //given
        BookResponseDto expected = new BookResponseDto();
        expected.setId(book.getId());
        expected.setName(book.getName());
        expected.setYear(book.getYear());
        expected.setAuthor(authorMapper.authorToResponseDtoShort(author));
        book.setAuthor(author);
        //when
        BookResponseDto actual = bookMapper.toBookResponseDto(book);
        BookResponseDto actualNull = bookMapper.toBookResponseDto(null);
        //that
        assertThat(actual).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
        assertThat(actualNull).isNull();
    }

    @Test
    void toBookResponseDtoList() {
        //given
        Book book1 = CreatedData.createBook(2);
        BookResponseDto dto1 = new BookResponseDto();
        dto1.setId(book.getId());
        dto1.setName(book.getName());
        dto1.setYear(book.getYear());
        dto1.setAuthor(authorMapper.authorToResponseDtoShort(author));
        BookResponseDto dto2 = new BookResponseDto();
        dto2.setId(book1.getId());
        dto2.setName(book1.getName());
        dto2.setYear(book1.getYear());
        dto2.setAuthor(authorMapper.authorToResponseDtoShort(author));
        book1.setAuthor(author);
        book.setAuthor(author);
        List<BookResponseDto> expectedList = List.of(dto1, dto2);
        //when
        List<BookResponseDto> actual = bookMapper.toBookResponseDtoList(List.of(book, book1));
        List<BookResponseDto> actualNull = bookMapper.toBookResponseDtoList(null);
        //that
        assertThat(actual).isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
        assertThat(actualNull).isNull();
    }

    @Test
    void fromBookDto() {
        //given
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setName(book.getName());
        bookDto.setYear(book.getYear());
        bookDto.setAuthor(author.getId());
        bookDto.setPublishers(List.of(publisher.getId()));
        book.setAuthor(author);
        book.addPublisher(publisher);
        //when
        Book actual = bookMapper.fromBookDto(bookDto);
        Book actualNull = bookMapper.fromBookDto(null);
        //that
        assertThat(actual).isNotNull()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(book);
        assertThat(actualNull).isNull();
    }

    @Test
    void toBookDto() {
        //given
        BookDto expected = new BookDto();
        expected.setId(book.getId());
        expected.setName(book.getName());
        expected.setYear(book.getYear());
        expected.setAuthor(author.getId());
        expected.setPublishers(List.of(publisher.getId()));
        book.setAuthor(author);
        book.addPublisher(publisher);
        //when
        BookDto actual = bookMapper.toBookDto(book);
        BookDto actualNull = bookMapper.toBookDto(null);
        //that
        assertThat(actual).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
        assertThat(actualNull).isNull();
    }

    @Test
    void fromBookUpdateDto() {
        //given
        BookUpdateDto bookUpdateDto = new BookUpdateDto();
        bookUpdateDto.setName(book.getName());
        bookUpdateDto.setYear(book.getYear());
        bookUpdateDto.setAuthor(author.getId());
        book.setAuthor(author);
        //when
        Book actual = bookMapper.fromBookUpdateDto(bookUpdateDto);
        actual.setId(book.getId());
        Book actualNull = bookMapper.fromBookUpdateDto(null);
        //that
        assertThat(actual).isNotNull()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(book);
        assertThat(actualNull).isNull();
    }

    @Test
    void toAuthorFromId() {
        //given
        long authorId = 1;
        Author expected = Author.builder().id(1L).build();
        //when
        Author actual = bookMapper.toAuthorFromId(authorId);
        //that
        assertThat(actual).isNotNull()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }

    @Test
    void toPublishersFromIds() {
        //given
        List<Long> publisherIds = List.of(1L, 2L);
        Publisher publisher1 = Publisher.builder().id(1L).build();
        Publisher publisher2 = Publisher.builder().id(2L).build();
        List<Publisher> expected = List.of(publisher1, publisher2);
        //when
        List<Publisher> actual = bookMapper.toPublishersFromIds(publisherIds);
        //that
        assertThat(actual).isNotEmpty()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }

    @Test
    void toPublishersIdFromPublisher() {
        //given
        List<Long> expected = List.of(1L, 2L);
        Publisher publisher1 = Publisher.builder().id(1L).build();
        Publisher publisher2 = Publisher.builder().id(2L).build();
        List<Publisher> publishers = List.of(publisher1, publisher2);
        //when
        List<Long> actual = bookMapper.toPublishersIdFromPublisher(publishers);
        //that
        assertThat(actual).isNotEmpty()
                .isEqualTo(expected);
    }
}