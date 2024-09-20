package ru.practicum.servlet.dto.book;

public class BookUpdateDto {
    private String name;
    private long author;
    private int year;

    public BookUpdateDto() {
    }

    public BookUpdateDto(String name, long author, int year) {
        this.name = name;
        this.author = author;
        this.year = year;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
