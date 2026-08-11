package com.kauabrandon.todolist.dtos;

import com.kauabrandon.todolist.models.Task;

public record TaskResponseDTO(Long id, String description, Long userId) {
    public TaskResponseDTO(Task task) {
        this(task.getId(), task.getDescription(), task.getUser().getId());
    }
}