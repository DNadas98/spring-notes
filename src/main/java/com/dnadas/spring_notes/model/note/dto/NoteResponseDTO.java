package com.dnadas.spring_notes.model.note.dto;

import java.util.Date;

public record NoteResponseDTO(Long id, String title, String content, Date created_at) {
}
