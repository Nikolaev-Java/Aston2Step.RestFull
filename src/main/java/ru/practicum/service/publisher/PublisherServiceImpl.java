package ru.practicum.service.publisher;

import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Publisher;
import ru.practicum.repository.CrudRepository;
import ru.practicum.service.ServiceUtils;

import java.util.List;

public class PublisherServiceImpl implements PublisherService {
    private final CrudRepository<Publisher, Long> repository;

    public PublisherServiceImpl(CrudRepository<Publisher, Long> repository) {
        this.repository = repository;
    }

    @Override
    public List<Publisher> getAllPublishers() {
        return repository.findAll();
    }

    @Override
    public Publisher getById(long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Publisher with id " + id + " not found"));
    }

    @Override
    public Publisher createPublisher(Publisher publisher) {
        return repository.save(publisher);
    }

    @Override
    public Publisher update(Publisher publisher, long id) {
        Publisher publisherToUpdate = repository.findById(id).
                orElseThrow(() -> new NotFoundException("Publisher with id " + id + " not found"));
        ServiceUtils.updateEntity(publisherToUpdate, publisher.getCity(),
                publisher1 -> publisher1.setCity(publisher.getCity()));
        ServiceUtils.updateEntity(publisherToUpdate, publisher.getName(),
                publisher1 -> publisher1.setName(publisher.getName()));
        return repository.save(publisherToUpdate, id);
    }

    @Override
    public boolean deleteById(long id) {
        return repository.delete(id);
    }
}
