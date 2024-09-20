package ru.practicum.model;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private long id;
    private String name;
    private Author author;
    private int year;
    private List<Publisher> publishers = new ArrayList<>();

    public Book() {
    }

    public Book(long id, String name, Author author, int year, List<Publisher> publishers) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.year = year;
        if (publishers != null) {
            this.publishers = publishers;
        }
    }

    public static BookBuilder builder() {
        return new BookBuilder();
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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Publisher> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<Publisher> publishers) {
        this.publishers = publishers;
    }

    public void addPublisher(Publisher publisher) {
        this.publishers.add(publisher);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;

        return id == book.id && year == book.year && name.equals(book.name);
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(id);
        result = 31 * result + name.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + year;
        return result;
    }

    public static class BookBuilder {
        private long id;
        private String name;
        private Author author;
        private int year;
        private List<Publisher> publishers;

        BookBuilder() {
        }

        public BookBuilder id(long id) {
            this.id = id;
            return this;
        }

        public BookBuilder name(String name) {
            this.name = name;
            return this;
        }

        public BookBuilder author(Author author) {
            this.author = author;
            return this;
        }

        public BookBuilder year(int year) {
            this.year = year;
            return this;
        }

        public BookBuilder publishers(List<Publisher> publishers) {
            this.publishers = publishers;
            return this;
        }

        public Book build() {
            return new Book(this.id, this.name, this.author, this.year, this.publishers);
        }
    }

    @Override
    public String toString() {
        return "Book{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", author=" + author +
               ", year=" + year +
               ", publishers=" + publishers +
               '}';
    }
}
