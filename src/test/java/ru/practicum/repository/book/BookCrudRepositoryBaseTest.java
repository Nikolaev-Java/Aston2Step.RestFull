package ru.practicum.repository.book;

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
import ru.practicum.repository.mapping.book.BookResultSetExtractor;
import ru.practicum.repository.mapping.book.BookRowMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
class BookCrudRepositoryBaseTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withInitScripts("./schema.sql", "./data.sql");
    private ConnectionManager connectionManager;
    private BookCrudRepository repository;

    @BeforeEach
    void setUp() {
        connectionManager = new TestPostgresConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        repository = new BookCrudRepositoryBase(connectionManager,
                new BookRowMapper(),
                new BookResultSetExtractor());
        JdbcDatabaseDelegate jdbcDatabaseDelegate = new JdbcDatabaseDelegate(postgres, "");
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "./data.sql");
    }

    @Test
    @DisplayName("Test created book functionality")
    void givenBookCrudRepository_whenCreateBook_thenCorrect() {
        //given
        Book book = Book.builder()
                .name("HarryPotter")
                .year(222)
                .author(Author.builder().id(1).build()).build();
        Publisher publisher = Publisher.builder().id(1).build();
        book.addPublisher(publisher);
        //when
        Book saved = repository.save(book);
        //that
        assertThat(saved).isNotNull()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(book);
    }

    @Test
    @DisplayName("Test created book, does not exist publisher functionality")
    void givenBookCrudRepository_whenCreateBookNotExistPublisher_thenException() {
        //given
        Book book = Book.builder()
                .name("HarryPotter")
                .year(222)
                .author(Author.builder().id(1).build()).build();
        Publisher publisher = Publisher.builder().id(33).build();
        book.addPublisher(publisher);
        //when

        //that
        assertThatThrownBy(() -> repository.save(book))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("violates foreign key constraint");
    }

    @Test
    @DisplayName("Test created book null param functionality")
    void givenBookCrudRepository_whenCreateBookNullParam_thenException() {
        //given
        Book book = Book.builder()
                .year(222)
                .author(Author.builder().id(1).build()).build();
        Publisher publisher = Publisher.builder().id(1).build();
        book.addPublisher(publisher);
        //when

        //that
        assertThatThrownBy(() -> repository.save(book))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ERROR: null value");
    }

    @Test
    @DisplayName("Test find by id book functionality")
    void giveBookCrudRepository_whenFindBook_thenCorrect() {
        //given
        Author authorBook = CreatedData.createAuthor(1);
        Book expected = CreatedData.createBook(1);
        Publisher publisher1 = CreatedData.createPublisher(1);
        Publisher publisher4 = CreatedData.createPublisher(4);
        Publisher publisher2 = CreatedData.createPublisher(2);
        expected.setAuthor(authorBook);
        List<Publisher> publishersBook1 = List.of(publisher1, publisher4, publisher2);
        expected.setPublishers(publishersBook1);
        //when
        Optional<Book> actual = repository.findById(expected.getId());
        //that
        assertThat(actual).isPresent()
                .get()
                .isEqualTo(expected);
        assertThat(actual.get().getAuthor()).isEqualTo(authorBook);
        assertThat(actual.get().getPublishers()).hasSize(publishersBook1.size()).hasSameElementsAs(publishersBook1);
    }

    @Test
    @DisplayName("Test find by id book not existing functionality")
    void givenBookCrudRepository_whenFindBookNotFound_thenResultEmpty() {
        //given
        //when
        Optional<Book> actual = repository.findById(99L);
        //that
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("Test delete book functionality")
    void givenBookCrudRepository_whenDeleteBook_thenCorrect() {
        //given
        //when
        boolean actual = repository.delete(1L);
        //that
        assertThat(actual).isTrue();
        assertThat(repository.findById(1L)).isEmpty();
    }

    @Test
    @DisplayName("Test delete book not exciting functionality")
    void givenBookCrudRepository_whenDeleteBookNotExciting_thenCorrect() {
        //given
        //when
        boolean actual = repository.delete(99L);
        //that
        assertThat(actual).isFalse();
        assertThat(repository.findAll()).isNotEmpty()
                .size().isEqualTo(5);
    }

    @Test
    @DisplayName("Test update book functionality")
    void givenBookCrudRepository_whenUpdateBook_thenCorrect() {
        //given
        Book updateBook = Book.builder().id(1).author(Author.builder().id(1).build()).name("update").build();
        //when
        Book actual = repository.save(updateBook, updateBook.getId());
        //that
        assertThat(actual).isNotNull()
                .isEqualTo(updateBook);
        assertThat(repository.findAll()).isNotEmpty()
                .size().isEqualTo(5);
    }

    @Test
    @DisplayName("Test update book name null functionality")
    void givenBookCrudRepository_whenUpdateBookNameNull_thenException() {
        //given
        Book updateBook = Book.builder().id(1).author(Author.builder().id(1).build()).build();
        //when
        //that
        assertThatThrownBy(() -> repository.save(updateBook, updateBook.getId())).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ERROR: null value");
    }

    @Test
    @DisplayName("Test update book not exist functionality")
    void givenBookCrudRepository_whenUpdateNotExist_thenException() {
        //given
        Book updateBook = Book.builder().id(66).author(Author.builder().id(1).build()).build();
        //when
        //that
        assertThatThrownBy(() -> repository.save(updateBook, updateBook.getId())).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to update book");
    }

    @Test
    @DisplayName("Test find all book functionality")
    void givenBookCrudRepository_whenFindALlBook_thenCorrect() {
        //given
        Book book1 = CreatedData.createBook(1);
        Book book2 = CreatedData.createBook(2);
        Book book3 = CreatedData.createBook(3);
        Book book4 = CreatedData.createBook(4);
        Book book5 = CreatedData.createBook(5);
        List<Book> expected = List.of(book1, book2, book3, book4, book5);
        //when
        List<Book> actual = repository.findAll();
        //that
        assertThat(actual).isNotEmpty()
                .isEqualTo(expected);
    }
}