package ru.practicum.servlet.dto.author;

import ru.practicum.servlet.dto.book.BookWithPublisherDto;

import java.util.List;

public class AuthorResponseDto {
    private long id;
    private String firstName;
    private String lastName;
    private List<BookWithPublisherDto> books;

    public AuthorResponseDto() {
    }

    public AuthorResponseDto(long id, String firstName, String lastName, List<BookWithPublisherDto> books) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.books = books;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<BookWithPublisherDto> getBooks() {
        return books;
    }

    public void setBooks(List<BookWithPublisherDto> books) {
        this.books = books;
    }
}
