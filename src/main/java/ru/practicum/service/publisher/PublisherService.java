package ru.practicum.service.publisher;

import ru.practicum.model.Publisher;

import java.util.List;

public interface PublisherService {
    List<Publisher> getAllPublishers();

    Publisher getById(long id);

    Publisher createPublisher(Publisher publisher);

    Publisher update(Publisher publisher, long id);

    boolean deleteById(long id);
}
