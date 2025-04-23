package ru.sfu.db.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sfu.db.models.User;
import ru.sfu.db.models.UserSettings;

@Repository
public interface UserSettingsRepository extends CrudRepository<UserSettings, Long> {
}
