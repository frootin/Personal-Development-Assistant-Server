package ru.sfu.db.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfu.db.models.Task;
import ru.sfu.db.repositories.TaskRepository;

@Service
public class TaskService {
    private TaskRepository repository;


    @Autowired
    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public void save(Task task) {
        repository.save(task);
    }

    public Task findById(long id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(long id) {
        repository.delete(findById(id));
    }
}
