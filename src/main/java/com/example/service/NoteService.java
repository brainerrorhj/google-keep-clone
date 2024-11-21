
import com.example.model.Note;
import com.example.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public Note save(Note note) {
        return noteRepository.save(note);
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }
}
