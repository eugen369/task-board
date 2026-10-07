package task_board_backend.dev.ed.task_board_backend.dto;

import task_board_backend.dev.ed.task_board_backend.entity.TaskStatus;
import java.time.LocalDate;

public record TaskDto(
        Long id,
        String title,
        String description,
        TaskStatus status,
        LocalDate dueDate) {

}
