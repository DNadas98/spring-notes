package com.dnadas.spring_notes.controller.dto;

import java.time.LocalDateTime;

public record NoteResponseDTO(Long id, String title, String content, LocalDateTime created_at) {
}
