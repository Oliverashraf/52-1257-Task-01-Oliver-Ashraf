package s52_1257.t_10.oliver_ashraf.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.ClassPathResource;
import s52_1257.t_10.oliver_ashraf.models.User;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private List<User> users;
    private java.io.File jsonFile;

    public UserRepository() {
        InputStream inputStream = getClass().getResourceAsStream("/users.json");
        if (inputStream == null) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to read users.json");
        }

        try {
            this.jsonFile = new java.io.File(getClass().getResource("/users.json").toURI());
        } catch (Exception e) {
            this.jsonFile = new java.io.File("/data/users.json");
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Use ArrayList to ensure the list is mutable
            this.users = new ArrayList<>(objectMapper.readValue(inputStream, new TypeReference<List<User>>() {}));
        } catch (Exception e) {
            this.users = new ArrayList<>();
        }
    }

    public List<User> findAll() {
        return users;
    }

    public Optional<User> findById(String id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    public User save(User user) {
        users.add(user);
        saveToFile();
        return user;
    }

    public Optional<User> update(String id, User updated) {
        return findById(id).map(existingUser -> {
            existingUser.setUsername(updated.getUsername());
            existingUser.setEmail(updated.getEmail());
            saveToFile();
            return existingUser;
        });
    }

    public boolean deleteById(String id) {
        boolean removed = users.removeIf(u -> u.getId().equals(id));
        if (removed) {
            saveToFile();
        }
        return removed;
    }

    private void saveToFile() {
        try {
            new ObjectMapper().writeValue(jsonFile, users);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to write to users.json");
        }
    }
}