package ru.practicum.service.author;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Author;
import ru.practicum.repository.author.AuthorCrudRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {
    @Mock
    private AuthorCrudRepository authorCrudRepository;
    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    @DisplayName("Update author (full param) functionality")
    void givenAuthorService_whenUpdateAuthor_thenUpdateAuthor() {
        //given
        Author author = Author.builder().id(1).firstName("test").lastName("test").build();
        BDDMockito.given(authorCrudRepository.findById(author.getId())).willReturn(Optional.of(author));
        BDDMockito.given(authorCrudRepository.save(author, author.getId())).willReturn(author);
        //when
        Author actual = authorService.update(author, author.getId());
        //that
        assertThat(actual).isEqualTo(author);
        verify(authorCrudRepository, times(1)).save(author, author.getId());
    }

    @Test
    @DisplayName("Update author not exist functionality")
    void givenAuthorService_whenUpdateAuthorNotExist_thenException() {
        //given
        Author author = Author.builder().id(1).firstName("test").lastName("test").build();
        BDDMockito.given(authorCrudRepository.findById(anyLong())).willThrow(NotFoundException.class);
        //when
        //that
        assertThrows(NotFoundException.class, () -> authorService.update(author, author.getId()));
        verify(authorCrudRepository, times(0)).save(author, author.getId());
    }

    @Test
    @DisplayName("Update author (one param) functionality")
    void givenAuthorService_whenUpdateAuthorOneParam_thenUpdateAuthor() {
        //given
        Author author = Author.builder().id(1).firstName("update").lastName("test").build();
        Author updatedAuthor = Author.builder().firstName("update").build();
        BDDMockito.given(authorCrudRepository.findById(author.getId())).willReturn(Optional.of(author));
        BDDMockito.given(authorCrudRepository.save(author, author.getId())).willReturn(author);
        //when
        Author actual = authorService.update(updatedAuthor, author.getId());
        //that
        assertThat(actual).isEqualTo(author);
        verify(authorCrudRepository, times(1)).save(author, author.getId());
    }
}