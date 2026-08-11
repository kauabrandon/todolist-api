package com.kauabrandon.todolist.dtos;

import com.kauabrandon.todolist.models.User;
import java.util.List;

public record UserResponseDTO(Long id, String username, List<TaskResponseDTO> tasks) {
    public UserResponseDTO(User user) {
        this(user.getId(), user.getUsername(),
                user.getTasks().stream().map(TaskResponseDTO::new).toList());
    }
}