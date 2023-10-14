package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private final Map<Integer, User> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        Arrays.asList(
                new User("admin", "admin@adm-mail.com", "admin", Collections.singleton(Role.ADMIN)),
                new User("user", "user@mail.com", "user", Collections.singleton(Role.USER)),
                new User("bothRoles", "bothRoles@mail.com", "bothRoles", new HashSet<>(Arrays.asList(Role.USER, Role.ADMIN)))
        ).forEach(this::save);
    }

    @Override
    public boolean delete(int id) {
        log.info("Delete user with id: {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("Save user: {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
        }
        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public User get(int id) {
        log.info("Get user with id: {}", id);
        return repository.get(id);
    }

    @Override
    public Collection<User> getAll() {
        log.info("Get all users");
        return repository.values().stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(@NonNull String email) {
        log.info("Get user by email: {}", email);
        return repository.values().stream()
                .filter(user -> email.equalsIgnoreCase(user.getEmail()))
                .findAny()
                .orElse(null);
    }
}
