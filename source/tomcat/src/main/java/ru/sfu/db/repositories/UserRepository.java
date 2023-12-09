package ru.sfu.db.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sfu.db.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
