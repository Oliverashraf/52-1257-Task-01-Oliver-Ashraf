package s52_1257.t_10.oliver_ashraf.services;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import s52_1257.t_10.oliver_ashraf.models.Note;
import s52_1257.t_10.oliver_ashraf.repositories.NoteRepository;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    // Constructor injection
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note getNoteById(String id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
    }

    public List<Note> getNotesByUserId(String userId) {
        List<Note> notes = noteRepository.findByUserId(userId);
        if (notes.isEmpty()) {
            // Requirement says throw 404 if not found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No notes found for this user");
        }
        return notes;
    }

    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    public Note updateNote(String id, Note note) {
        return noteRepository.update(id, note)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
    }

    public void deleteNote(String id) {
        if (!noteRepository.deleteById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found");
        }
    }
}