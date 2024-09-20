package ru.practicum.model;

import java.util.ArrayList;
import java.util.List;

public class Author {
    private long id;
    private String firstName;
    private String lastName;
    private List<Book> books = new ArrayList<>();

    public Author() {
    }

    public Author(long id, String firstName, String lastName, List<Book> books) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        if (books != null) {
            this.books = books;
        }
    }

    public static AuthorBuilder builder() {
        return new AuthorBuilder();
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
        if (!(o instanceof Author author)) return false;

        return id == author.id && firstName.equals(author.firstName) && lastName.equals(author.lastName);
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(id);
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }

    public static class AuthorBuilder {
        private long id;
        private String firstName;
        private String lastName;
        private List<Book> books;

        AuthorBuilder() {
        }

        public AuthorBuilder id(long id) {
            this.id = id;
            return this;
        }

        public AuthorBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AuthorBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AuthorBuilder books(List<Book> books) {
            this.books = books;
            return this;
        }

        public Author build() {
            return new Author(this.id, this.firstName, this.lastName, this.books);
        }
    }

    @Override
    public String toString() {
        return "Author{" +
               "id=" + id +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", books=" + books +
               '}';
    }
}
