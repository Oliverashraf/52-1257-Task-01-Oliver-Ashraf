package s52_1257.t_10.oliver_ashraf.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import s52_1257.t_10.oliver_ashraf.models.Note;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class NoteRepository {

    private List<Note> notes;
    private java.io.File jsonFile;

    public NoteRepository() {
        InputStream inputStream = getClass().getResourceAsStream("/notes.json");
        if (inputStream == null) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to read notes.json");
        }

        try {
            this.jsonFile = new java.io.File(getClass().getResource("/notes.json").toURI());
        } catch (Exception e) {
            this.jsonFile = new java.io.File("/data/notes.json");
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.notes = new ArrayList<>(objectMapper.readValue(inputStream, new TypeReference<List<Note>>() {}));
        } catch (Exception e) {
            this.notes = new ArrayList<>();
        }
    }

    public List<Note> findAll() {
        return notes;
    }

    public Optional<Note> findById(String id) {
        return notes.stream().filter(n -> n.getId().equals(id)).findFirst();
    }

    public List<Note> findByUserId(String userId) {
        return notes.stream()
                .filter(n -> n.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public Note save(Note note) {
        notes.add(note);
        saveToFile();
        return note;
    }

    public Optional<Note> update(String id, Note updated) {
        return findById(id).map(existingNote -> {
            existingNote.setTitle(updated.getTitle());
            existingNote.setContent(updated.getContent());
            existingNote.setUserId(updated.getUserId());
            saveToFile();
            return existingNote;
        });
    }

    public boolean deleteById(String id) {
        boolean removed = notes.removeIf(n -> n.getId().equals(id));
        if (removed) {
            saveToFile();
        }
        return removed;
    }

    private void saveToFile() {
        try {
            new ObjectMapper().writeValue(jsonFile, notes);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to write to notes.json");
        }
    }
}