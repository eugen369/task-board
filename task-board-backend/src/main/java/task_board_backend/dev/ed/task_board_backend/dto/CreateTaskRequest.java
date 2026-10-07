package task_board_backend.dev.ed.task_board_backend.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record CreateTaskRequest(
        @NotBlank(message = "Titel darf nicht leer sein") String title,
        String description,
        LocalDate dueDate) {

}
