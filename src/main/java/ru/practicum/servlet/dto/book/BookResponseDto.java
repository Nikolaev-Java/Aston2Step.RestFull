package ru.practicum.servlet.dto.book;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.practicum.servlet.dto.author.AuthorResponseDtoShort;

public class BookResponseDto {
    private long id;
    private String name;
    private int year;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AuthorResponseDtoShort author;

    public BookResponseDto() {
    }

    public BookResponseDto(long id, String name, int year, AuthorResponseDtoShort author) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AuthorResponseDtoShort getAuthor() {
        return author;
    }

    public void setAuthor(AuthorResponseDtoShort author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
