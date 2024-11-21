package com.example.notecontroller;

import com.example.note.Note;
import com.example.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    // Folder to store images
    private static final String IMAGE_FOLDER = "C://Users/Hi//OneDrive//Tài liệu//NetBeansProjects//demoooooo//uploads/";

    // Ensure folder exists
    static {
        File uploadDir = new File(IMAGE_FOLDER);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    // Get all notes
    @GetMapping
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    // Get note by ID
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        Optional<Note> note = noteRepository.findById(id);
        return note.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create new note with optional image upload
    @PostMapping
    public ResponseEntity<?> createNote(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) MultipartFile image) {
        try {
            if (title == null || title.isEmpty() || content == null || content.isEmpty()) {
                return ResponseEntity.badRequest().body("Title and content are required.");
            }

            String imageName = null;
            if (image != null && !image.isEmpty()) {
                imageName = saveImage(image);
            }

            // Create new note
            Note newNote = new Note(title, content, imageName);
            Note savedNote = noteRepository.save(newNote);
            return ResponseEntity.ok(savedNote);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to save image: " + e.getMessage());
        }
    }

    // Update an existing note by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) MultipartFile image) {
        try {
            Optional<Note> note = noteRepository.findById(id);
            if (note.isPresent()) {
                Note existingNote = note.get();
                existingNote.setTitle(title);
                existingNote.setContent(content);

                if (image != null && !image.isEmpty()) {
                    String imageName = saveImage(image);
                    existingNote.setImage(imageName);
                }

                // Save updated note
                Note updatedNote = noteRepository.save(existingNote);
                return ResponseEntity.ok(updatedNote);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to save image: " + e.getMessage());
        }
    }

    // Delete note by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        if (noteRepository.existsById(id)) {
            noteRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Helper method to save image
    private String saveImage(MultipartFile image) throws IOException {
        // Clean and generate a unique image name to avoid conflicts
        String originalFileName = StringUtils.cleanPath(image.getOriginalFilename());
        String imageName = UUID.randomUUID().toString() + "_" + originalFileName;

        Path imagePath = Paths.get(IMAGE_FOLDER, imageName);

        // Create directories if they don't exist
        Files.createDirectories(imagePath.getParent());

        // Save the image to the disk
        image.transferTo(imagePath.toFile());

        return imageName;
    }
}
