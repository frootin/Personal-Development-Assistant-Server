package ru.sfu.db.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sfu.db.models.Category;
import ru.sfu.db.models.User;
import ru.sfu.db.models.UserSettings;
import ru.sfu.db.repositories.CategoryRepository;
import ru.sfu.db.repositories.UserRepository;
import ru.sfu.db.repositories.UserSettingsRepository;
import ru.sfu.exceptions.PasswordNoMatchException;
import ru.sfu.exceptions.UsernameAlreadyExistsException;
import ru.sfu.objects.RegisterUserDto;

@Service
public class UserService {
    private UserRepository repository;
    private UserSettingsRepository settingsRepository;
    private CategoryRepository categoryRepository;
    //private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository repository, UserSettingsRepository settingsRepository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.settingsRepository = settingsRepository;
        this.categoryRepository = categoryRepository;
        //this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserDetailsService userDetailsService() {
        return this::loadUserByUsername;
    }

    public UserDetailsService getUserDetailsService() {
        return this::loadUserByUsername;
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
        //newUser.setPassword(bCryptPasswordEncoder.encode(registerUserDto.getPassword()));
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

    /**public User saveUser(User newUser) throws UsernameAlreadyExistsException {
        try {
            newUser.setPassword(
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
    }*/

    public User create(User user, String timezone) {
        if (repository.existsByUsername(user.getUsername())) {
            // Заменить на свои исключения
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        User newUser = save(user);
        UserSettings settings = new UserSettings(newUser, timezone);
        //settings.setUserId(new User(newUser.getId()));
        System.out.println(newUser);
        System.out.println(settings);
        settingsRepository.save(settings);
        categoryRepository.saveAll(Category.getInitialCategories(newUser));
        return newUser;
    }

    public User save(User user) {
        return repository.save(user);
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    public User loadUserByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}
