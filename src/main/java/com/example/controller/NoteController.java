package com.example.controller;

import com.example.model.Note;
import com.example.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    // Tạo ghi chú mới
    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        Note createdNote = noteService.createNote(note);
        return ResponseEntity.ok(createdNote);
    }

    // Lấy danh sách ghi chú
    @GetMapping
    public ResponseEntity<List<Note>> getNotes() {
        List<Note> notes = noteService.getAllNotes();
        return ResponseEntity.ok(notes);
    }

    // Cập nhật ghi chú
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note note) {
        Note updatedNote = noteService.updateNote(id, note);
        return ResponseEntity.ok(updatedNote);
    }

    // Xóa ghi chú
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.ok("Note deleted successfully");
    }
}
