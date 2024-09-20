package ru.practicum.servlet.dto.book;

import java.util.List;

public class BookDto {
    private long id;
    private String name;
    private long author;
    private int year;
    private List<Long> publishers;

    public BookDto() {
    }

    public BookDto(long id, String name, long author, int year, List<Long> publishers) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.year = year;
        this.publishers = publishers;
    }

    public List<Long> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<Long> publishers) {
        this.publishers = publishers;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public long getAuthor() {
        return author;
    }

    public void setAuthor(long author) {
        this.author = author;
    }
}
