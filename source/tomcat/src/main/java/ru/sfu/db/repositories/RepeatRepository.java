package ru.sfu.db.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sfu.db.models.Repeat;

@Repository
public interface RepeatRepository extends CrudRepository<Repeat, Long> {

}
