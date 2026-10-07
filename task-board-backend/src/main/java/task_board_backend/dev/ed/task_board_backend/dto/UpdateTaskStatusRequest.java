package task_board_backend.dev.ed.task_board_backend.dto;

import jakarta.validation.constraints.NotNull;
import task_board_backend.dev.ed.task_board_backend.entity.TaskStatus;

public record UpdateTaskStatusRequest(
    @NotNull(message = "Status darf nicht null sein")
    TaskStatus status
) {}
