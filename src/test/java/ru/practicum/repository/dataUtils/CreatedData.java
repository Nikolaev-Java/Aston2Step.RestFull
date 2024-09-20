package ru.practicum.repository.dataUtils;

import ru.practicum.model.Author;
import ru.practicum.model.Book;
import ru.practicum.model.Publisher;

public class CreatedData {
    public static Book createBook(int nameVariable) {
        return Book.builder()
                .id(nameVariable)
                .name("book" + nameVariable)
                .year(nameVariable)
                .build();
    }

    public static Author createAuthor(int nameVariable) {
        return Author.builder()
                .id(nameVariable)
                .firstName("name" + nameVariable)
                .lastName("lastName" + nameVariable)
                .build();
    }

    public static Publisher createPublisher(int nameVariable) {
        return Publisher.builder()
                .id(nameVariable)
                .name("publisher" + nameVariable)
                .city("city" + nameVariable)
                .build();
    }
}
