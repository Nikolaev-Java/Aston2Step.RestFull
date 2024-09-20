package ru.practicum.repository.author;

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
import ru.practicum.repository.mapping.author.AuthorResultSetExtractor;
import ru.practicum.repository.mapping.author.AuthorRowMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
class AuthorCrudRepositoryBaseTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withInitScript("./schema.sql");
    private ConnectionManager connectionManager;
    private AuthorCrudRepositoryBase repository;

    @BeforeEach
    void setUp() {
        connectionManager = new TestPostgresConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        repository = new AuthorCrudRepositoryBase(connectionManager,
                new AuthorRowMapper(),
                new AuthorResultSetExtractor());
        JdbcDatabaseDelegate jdbcDatabaseDelegate = new JdbcDatabaseDelegate(postgres, "");
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "./data.sql");
    }

    @Test
    @DisplayName("Test created author functionality")
    void givenAuthorCrudRepository_whenCreateAuthor_thenCorrect() {
        //given
        Author author = Author.builder()
                .firstName("Jon")
                .lastName("Smith")
                .build();
        //when
        Author actual = repository.save(author);
        //that
        assertThat(actual).isNotNull()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(author);
    }

    @Test
    @DisplayName("Test created author this name null functionality")
    void givenAuthorCrudRepository_whenCreateAuthorNameNull_thenException() {
        //given
        Author author = Author.builder()
                .firstName("Jon")
                .build();
        //when
        //that
        assertThatThrownBy(() -> repository.save(author)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ERROR: null value");
    }

    @Test
    @DisplayName("Test find by id author functionality")
    void givenAuthorCrudRepository_whenFindAuthor_thenCorrect() {
        //given
        Author expected = CreatedData.createAuthor(1);
        Book book1 = CreatedData.createBook(1);
        Book book2 = CreatedData.createBook(2);
        Book book5 = CreatedData.createBook(5);
        Publisher publisher1 = CreatedData.createPublisher(1);
        Publisher publisher2 = CreatedData.createPublisher(2);
        Publisher publisher3 = CreatedData.createPublisher(3);
        Publisher publisher4 = CreatedData.createPublisher(4);
        List<Publisher> publishersBook1 = List.of(publisher1, publisher4, publisher2);
        book1.setPublishers(publishersBook1);
        List<Publisher> publishersBook2 = List.of(publisher3);
        book2.setPublishers(publishersBook2);
        List<Publisher> publishersBook5 = List.of(publisher3);
        book5.setPublishers(publishersBook5);
        List<Book> authorsBook = List.of(book1, book5, book2);
        expected.setBooks(authorsBook);
        //when
        Optional<Author> actual = repository.findById(expected.getId());
        //that
        assertThat(actual).isPresent()
                .get()
                .isEqualTo(expected);
        assertThat(actual.get().getBooks()).hasSize(authorsBook.size()).hasSameElementsAs(authorsBook);
    }

    @Test
    @DisplayName("Test find by id author not existing functionality")
    void givenAuthorCrudRepository_whenFindAuthorNotFound_thenResultEmpty() {
        //given
        //when
        Optional<Author> actual = repository.findById(99L);
        //that
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("Test delete author functionality")
    void givenAuthorCrudRepository_whenDeleteAuthor_thenCorrect() {
        //given
        //when
        boolean actual = repository.delete(1L);
        //that
        assertThat(actual).isTrue();
        assertThat(repository.findById(1L)).isEmpty();
    }

    @Test
    @DisplayName("Test delete author not exciting functionality")
    void givenAuthorCrudRepository_whenDeleteAuthorNotExciting_thenCorrect() {
        //given
        //when
        boolean actual = repository.delete(99L);
        //that
        assertThat(actual).isFalse();
        assertThat(repository.findAll()).isNotEmpty()
                .size().isEqualTo(3);
    }

    @Test
    @DisplayName("Test update author functionality")
    void givenAuthorCrudRepository_whenUpdateAuthor_thenCorrect() {
        //given
        Author update = Author.builder().id(1).firstName("Jon").lastName("Smith").build();
        //when
        Author actual = repository.save(update, update.getId());
        assertThat(actual).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(update);
        assertThat(repository.findAll()).isNotEmpty()
                .size().isEqualTo(3);
    }

    @Test
    @DisplayName("Test update author not exist functionality")
    void givenAuthorCrudRepository_whenUpdateAuthorNotExist_thenException() {
        //given
        Author update = Author.builder().id(66).firstName("Jon").lastName("Smith").build();
        //when
        //that
        assertThatThrownBy(() -> repository.save(update, update.getId())).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to update author");
    }

    @Test
    @DisplayName("Test update author null param functionality")
    void givenAuthorCrudRepository_whenUpdateAuthorNullParam_thenException() {
        //given
        Author update = Author.builder().id(1).firstName("Jon").build();
        //when
        //that
        assertThatThrownBy(() -> repository.save(update, update.getId())).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ERROR: null value");
    }

    @Test
    @DisplayName("Test find all author functionality")
    void givenAuthorCrudRepository_whenFindALlAuthor_thenCorrect() {
        //given
        Author author1 = CreatedData.createAuthor(1);
        Author author2 = CreatedData.createAuthor(2);
        Author author3 = CreatedData.createAuthor(3);
        List<Author> expected = List.of(author1, author2, author3);
        //when
        List<Author> actual = repository.findAll();
        //that
        assertThat(actual).isNotEmpty()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Test find by id author without books functionality")
    void givenAuthorCrudRepository_whenFindAuthorWithout_thenCorrect() {
        //given
        Author expected = repository.save(CreatedData.createAuthor(5));
        //when
        Optional<Author> actual = repository.findById(expected.getId());
        //that
        assertThat(actual).isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringActualNullFields()
                .isEqualTo(expected);
    }
}