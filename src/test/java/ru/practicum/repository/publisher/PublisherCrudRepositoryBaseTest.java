package ru.practicum.repository.publisher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.practicum.db.ConnectionManager;
import ru.practicum.model.Author;
import ru.practicum.model.Book;
import ru.practicum.model.Publisher;
import ru.practicum.repository.TestPostgresConnection;
import ru.practicum.repository.dataUtils.CreatedData;
import ru.practicum.repository.mapping.publisher.PublisherResultSetExtractor;
import ru.practicum.repository.mapping.publisher.PublisherRowMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
class PublisherCrudRepositoryBaseTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withInitScripts("./schema.sql", "./data.sql");
    private ConnectionManager connectionManager;
    private PublisherCrudRepository repository;

    @BeforeEach
    void setUp() {
        connectionManager = new TestPostgresConnection(postgres.getJdbcUrl(), postgres.getUsername(),
                postgres.getPassword());
        repository = new PublisherCrudRepositoryBase(connectionManager,
                new PublisherResultSetExtractor(),
                new PublisherRowMapper());
        JdbcDatabaseDelegate jdbcDatabaseDelegate = new JdbcDatabaseDelegate(postgres, "");
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "./data.sql");
    }

    @Test
    @DisplayName("Test created publisher functionality")
    void givenPublisherCrudRepository_whenCreatePublisher_thenCorrect() {
        //given
        Publisher publisher = Publisher.builder().name("test").city("test").build();
        //when
        Publisher saved = repository.save(publisher);
        //that
        assertThat(saved).isNotNull()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(publisher);
    }

    @Test
    @DisplayName("Test created publisher null param functionality")
    void givenPublisherCrudRepository_whenCreatePublisherNullParam_thenException() {
        //given
        Publisher publisher = Publisher.builder().name("test").build();
        //when
        //that
        assertThatThrownBy(() -> repository.save(publisher)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ERROR: null value");
    }

    @Test
    @DisplayName("Test find by id publisher functionality")
    void givePublisherCrudRepository_whenFindPublisher_thenCorrect() {
        //given
        Author author1 = CreatedData.createAuthor(1);
        Author author2 = CreatedData.createAuthor(2);
        Book book1 = CreatedData.createBook(1);
        book1.setAuthor(author1);
        Book book3 = CreatedData.createBook(3);
        book3.setAuthor(author2);
        List<Book> publishersBook = List.of(book1, book3);
        Publisher expected = CreatedData.createPublisher(1);
        expected.setBooks(publishersBook);
        //when
        Optional<Publisher> actual = repository.findById(expected.getId());
        //that
        assertThat(actual).isPresent()
                .get()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Test find by id publisher not existing functionality")
    void givenPublisherCrudRepository_whenFindPublisherNotFound_thenResultEmpty() {
        //given
        //when
        Optional<Publisher> actual = repository.findById(99L);
        //that
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("Test delete publisher functionality")
    void givenPublisherCrudRepository_whenDeletePublisher_thenCorrect() {
        //given
        //when
        boolean actual = repository.delete(1L);
        //that
        assertThat(actual).isTrue();
        assertThat(repository.findById(1L)).isEmpty();
    }

    @Test
    @DisplayName("Test delete publisher not exciting functionality")
    void givenPublisherCrudRepository_whenDeletePublisherNotExciting_thenCorrect() {
        //given
        //when
        boolean actual = repository.delete(99L);
        //that
        assertThat(actual).isFalse();
        assertThat(repository.findAll()).isNotEmpty()
                .size().isEqualTo(4);
    }

    @Test
    @DisplayName("Test update publisher functionality")
    void givenPublisherCrudRepository_whenUpdatePublisher_thenCorrect() {
        //given
        Publisher updatedPublisher = Publisher.builder().id(1).name("test").city("test").build();
        //when
        Publisher actual = repository.save(updatedPublisher, updatedPublisher.getId());
        //that
        assertThat(actual).isNotNull()
                .isEqualTo(updatedPublisher);
        assertThat(repository.findAll()).isNotEmpty()
                .size().isEqualTo(4);
    }

    @Test
    @DisplayName("Test update publisher null param functionality")
    void givenPublisherCrudRepository_whenUpdatePublisherNullParam_thenException() {
        //given
        Publisher updatedPublisher = Publisher.builder().id(1).name("test").build();
        //when
        //that
        assertThatThrownBy(() -> repository.save(updatedPublisher, updatedPublisher.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ERROR: null value");
    }

    @Test
    @DisplayName("Test update publisher not exist functionality")
    void givenPublisherCrudRepository_whenUpdatePublisherNotExist_thenException() {
        //given
        Publisher updatedPublisher = Publisher.builder().id(156).name("test").city("test").build();
        //when
        //that
        assertThatThrownBy(() -> repository.save(updatedPublisher, updatedPublisher.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to update publisher");
    }

    @Test
    @DisplayName("Test find all publisher functionality")
    void givenPublisherCrudRepository_whenFindALlPublisher_thenCorrect() {
        //given
        Publisher publisher1 = CreatedData.createPublisher(1);
        Publisher publisher2 = CreatedData.createPublisher(2);
        Publisher publisher3 = CreatedData.createPublisher(3);
        Publisher publisher4 = CreatedData.createPublisher(4);
        List<Publisher> expected = List.of(publisher1, publisher2, publisher3, publisher4);
        //when
        List<Publisher> actual = repository.findAll();
        //that
        assertThat(actual).isNotEmpty()
                .isEqualTo(expected);
    }
}