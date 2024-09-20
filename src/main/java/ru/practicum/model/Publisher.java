package ru.practicum.model;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private long id;
    private String name;
    private String city;
    private List<Book> books = new ArrayList<>();

    public Publisher() {
    }

    public Publisher(long id, String name, String city, List<Book> books) {
        this.id = id;
        this.name = name;
        this.city = city;
        if (books != null) {
            this.books = books;
        }
    }

    public static PublisherBuilder builder() {
        return new PublisherBuilder();
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publisher publisher)) return false;

        return id == publisher.id && name.equals(publisher.name) && city.equals(publisher.city);
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(id);
        result = 31 * result + name.hashCode();
        result = 31 * result + city.hashCode();
        return result;
    }

    public static class PublisherBuilder {
        private long id;
        private String name;
        private String city;
        private List<Book> books;

        PublisherBuilder() {
        }

        public PublisherBuilder id(long id) {
            this.id = id;
            return this;
        }

        public PublisherBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PublisherBuilder city(String city) {
            this.city = city;
            return this;
        }

        public PublisherBuilder books(List<Book> books) {
            this.books = books;
            return this;
        }

        public Publisher build() {
            return new Publisher(this.id, this.name, this.city, this.books);
        }
    }

    @Override
    public String toString() {
        return "Publisher{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", city='" + city + '\'' +
               ", books=" + books +
               '}';
    }
}
