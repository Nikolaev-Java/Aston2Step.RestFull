package ru.practicum.service.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Book;
import ru.practicum.repository.book.BookCrudRepository;
import ru.practicum.repository.dataUtils.CreatedData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookCrudRepository repository;
    @InjectMocks
    private BookServiceImpl service;

    @Test
    @DisplayName("Update author (full param) functionality")
    void givenBookService_whenUpdateBook_thenUpdateBook() {
        //given
        Book book = CreatedData.createBook(1);
        BDDMockito.given(repository.findById(book.getId())).willReturn(Optional.of(book));
        BDDMockito.given(repository.save(book, book.getId())).willReturn(book);
        //when
        Book actual = service.update(book, book.getId());
        //that
        assertThat(actual).isEqualTo(book);
        verify(repository, times(1)).save(book, book.getId());
    }

    @Test
    @DisplayName("Update author not exist functionality")
    void givenBookService_whenUpdateBookNotExist_thenException() {
        //given
        Book book = CreatedData.createBook(1);
        BDDMockito.given(repository.findById(anyLong())).willThrow(NotFoundException.class);
        //when
        //that
        assertThrows(NotFoundException.class, () -> service.update(book, book.getId()));
        verify(repository, times(0)).save(book, book.getId());
    }

    @Test
    @DisplayName("Update author (one param) functionality")
    void givenBookService_whenUpdateBookOneParam_thenUpdateBook() {
        //given
        Book book = Book.builder().id(1).name("update").author(CreatedData.createAuthor(1)).build();
        Book updatedBook = Book.builder().name("update").build();
        BDDMockito.given(repository.findById(book.getId())).willReturn(Optional.of(book));
        BDDMockito.given(repository.save(book, book.getId())).willReturn(book);
        //when
        Book actual = service.update(updatedBook, book.getId());
        //that
        assertThat(actual).isEqualTo(book);
        verify(repository, times(1)).save(book, book.getId());
    }
}