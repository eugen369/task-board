package task_board_backend.dev.ed.task_board_backend.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateBoardRequest(@NotBlank(message = "Name darf nicht leer sein") String name) {
}