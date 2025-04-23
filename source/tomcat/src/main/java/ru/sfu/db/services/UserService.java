package ru.sfu.db.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sfu.db.models.User;
import ru.sfu.db.models.UserSettings;
import ru.sfu.db.repositories.UserRepository;
import ru.sfu.db.repositories.UserSettingsRepository;
import ru.sfu.exceptions.PasswordNoMatchException;
import ru.sfu.exceptions.UsernameAlreadyExistsException;
import ru.sfu.objects.RegisterUserDto;

@Service
public class UserService {
    private UserRepository repository;
    private UserSettingsRepository settingsRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository repository, UserSettingsRepository settingsRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.repository = repository;
        this.settingsRepository = settingsRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow();
    }

    public User registerUser(RegisterUserDto registerUserDto) throws UsernameAlreadyExistsException, PasswordNoMatchException {
        User newUser = new User();
        if (repository.existsByUsername(registerUserDto.getUsername()) ||
                repository.existsByEmail(registerUserDto.getEmail())) {

            throw new UsernameAlreadyExistsException("Username or Email already exists");
        }
        if (!registerUserDto.getPassword().equals(registerUserDto.getRepeatPassword())) {
            throw new PasswordNoMatchException();
        }
        newUser.setPasshash(bCryptPasswordEncoder.encode(registerUserDto.getPassword()));
        newUser.setEmail(registerUserDto.getEmail());
        newUser.setUsername(registerUserDto.getUsername());
        newUser.setDisplayName(registerUserDto.getUsername());
        newUser = repository.save(newUser);
        System.out.println(newUser);
        UserSettings settings = new UserSettings(registerUserDto.getTimezone());
        settings.setUserId(new User(newUser.getId()));
        System.out.println(settings);
        settingsRepository.save(settings);
        return newUser;
    }

    public User saveUser(User newUser) throws UsernameAlreadyExistsException {
        try {
            newUser.setPasshash(
                    bCryptPasswordEncoder.encode(newUser.getPassword())
            );
            //Username has to be unique (exception)
            newUser.setUsername(newUser.getUsername());

            // Make sure that password and confirmPassword match
            // We don't persist or show the confirmPassword
            // newUser.setConfirmPassword(null);

            return repository.save(newUser);
        } catch (Exception e) {
            throw new UsernameAlreadyExistsException("Username '" + newUser.getUsername() + "' already exists");
        }
    }
}
