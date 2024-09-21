package ru.practicum.service.publisher;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Publisher;
import ru.practicum.repository.dataUtils.CreatedData;
import ru.practicum.repository.publisher.PublisherCrudRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PublisherServiceImplTest {

    @Mock
    private PublisherCrudRepository repository;
    @InjectMocks
    private PublisherServiceImpl service;

    @Test
    @DisplayName("Update author (full param) functionality")
    void givenAuthorService_whenUpdateAuthor_thenUpdateAuthor() {
        //given
        Publisher publisher = CreatedData.createPublisher(1);
        Publisher returnedRepository = Publisher.builder().id(1).name("test").city("test").build();
        BDDMockito.given(repository.findById(publisher.getId())).willReturn(Optional.of(returnedRepository));
        BDDMockito.given(repository.save(publisher, publisher.getId())).willReturn(publisher);
        //when
        Publisher actual = service.update(publisher, publisher.getId());
        //that
        assertThat(actual).isEqualTo(publisher);
        verify(repository, times(1)).save(publisher, publisher.getId());
    }

    @Test
    @DisplayName("Update author not exist functionality")
    void givenAuthorService_whenUpdateAuthorNotExist_thenException() {
        //given
        Publisher publisher = CreatedData.createPublisher(1);
        BDDMockito.given(repository.findById(anyLong())).willThrow(NotFoundException.class);
        //when
        //that
        assertThrows(NotFoundException.class, () -> service.update(publisher, publisher.getId()));
        verify(repository, times(0)).save(publisher, publisher.getId());
    }

    @Test
    @DisplayName("Update author (one param) functionality")
    void givenAuthorService_whenUpdateAuthorOneParam_thenUpdateAuthor() {
        //given
        Publisher publisher = Publisher.builder().id(1).name("update").city("test").build();
        Publisher returnedRepository = Publisher.builder().id(1).name("test").city("test").build();
        Publisher updatedBook = Publisher.builder().name("update").build();
        BDDMockito.given(repository.findById(publisher.getId())).willReturn(Optional.of(returnedRepository));
        BDDMockito.given(repository.save(publisher, publisher.getId())).willReturn(publisher);
        //when
        Publisher actual = service.update(updatedBook, publisher.getId());
        //that
        assertThat(actual).isEqualTo(publisher);
        verify(repository, times(1)).save(publisher, publisher.getId());
    }
}