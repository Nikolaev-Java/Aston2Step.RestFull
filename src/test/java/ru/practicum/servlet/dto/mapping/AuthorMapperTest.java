package ru.practicum.servlet.dto.mapping;

import org.junit.jupiter.api.Test;
import ru.practicum.model.Author;
import ru.practicum.model.Book;
import ru.practicum.repository.dataUtils.CreatedData;
import ru.practicum.servlet.dto.author.AuthorRequestDto;
import ru.practicum.servlet.dto.author.AuthorResponseDto;
import ru.practicum.servlet.dto.author.AuthorResponseDtoShort;
import ru.practicum.servlet.dto.book.BookWithPublisherDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorMapperTest {
    private AuthorMapper authorMapper = new AuthorMapperImpl();
    private BookMapper bookMapper = new BookMapperImpl();
    private Author author = CreatedData.createAuthor(1);
    private Book book = CreatedData.createBook(1);

    @Test
    void authorToResponseDto() {
        //given
        AuthorResponseDto expected = new AuthorResponseDto();
        expected.setId(author.getId());
        expected.setFirstName(author.getFirstName());
        expected.setLastName(author.getLastName());
        author.addBook(book);
        BookWithPublisherDto bookWithPublisherDto = bookMapper.toBookWithPublisherDto(book);
        expected.setBooks(List.of(bookWithPublisherDto));
        //when
        AuthorResponseDto actual = authorMapper.authorToResponseDto(author);
        AuthorResponseDto actualNull = authorMapper.authorToResponseDto(null);
        //that
        assertThat(actual).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
        assertThat(actualNull).isNull();
    }

    @Test
    void fromAuthorRequestDto() {
        //given
        Author expected = CreatedData.createAuthor(1);
        AuthorRequestDto authorRequestDto = new AuthorRequestDto();
        authorRequestDto.setFirstName(expected.getFirstName());
        authorRequestDto.setLastName(expected.getLastName());
        //when
        Author actual = authorMapper.fromAuthorRequestDto(authorRequestDto);
        actual.setId(author.getId());
        Author actualNull = authorMapper.fromAuthorRequestDto(null);
        //that
        assertThat(actual).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
        assertThat(actualNull).isNull();
    }

    @Test
    void authorToResponseDtoShort() {
        //given
        AuthorResponseDtoShort expected = new AuthorResponseDtoShort();
        expected.setId(author.getId());
        expected.setFirstName(author.getFirstName());
        expected.setLastName(author.getLastName());
        //when
        AuthorResponseDtoShort actual = authorMapper.authorToResponseDtoShort(author);
        AuthorResponseDtoShort actualNull = authorMapper.authorToResponseDtoShort(null);
        //that
        assertThat(actual).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
        assertThat(actualNull).isNull();
    }

    @Test
    void authorListToResponseDtoShortList() {
        //given
        Author author1 = CreatedData.createAuthor(2);
        AuthorResponseDtoShort dto1 = new AuthorResponseDtoShort();
        dto1.setId(author.getId());
        dto1.setFirstName(author.getFirstName());
        dto1.setLastName(author.getLastName());
        AuthorResponseDtoShort dto2 = new AuthorResponseDtoShort();
        dto2.setId(author1.getId());
        dto2.setFirstName(author1.getFirstName());
        dto2.setLastName(author1.getLastName());
        List<AuthorResponseDtoShort> expectedList = List.of(dto1, dto2);
        //when
        List<AuthorResponseDtoShort> actual = authorMapper.authorListToResponseDtoShortList(List.of(author, author1));
        List<AuthorResponseDtoShort> actualNull = authorMapper.authorListToResponseDtoShortList(null);
        //that
        assertThat(actual).isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
        assertThat(actualNull).isNull();
    }
}