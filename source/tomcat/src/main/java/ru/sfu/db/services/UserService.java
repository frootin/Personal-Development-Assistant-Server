package ru.sfu.db.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfu.db.models.User;
import ru.sfu.db.repositories.UserRepository;

@Service
public class UserService {
    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User findById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
