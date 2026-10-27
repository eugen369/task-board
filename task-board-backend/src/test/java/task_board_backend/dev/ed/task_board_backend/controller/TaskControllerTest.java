package task_board_backend.dev.ed.task_board_backend.controller;

import task_board_backend.dev.ed.task_board_backend.dto.CreateTaskRequest;
import task_board_backend.dev.ed.task_board_backend.dto.TaskDto;
import task_board_backend.dev.ed.task_board_backend.dto.UpdateTaskStatusRequest;
import task_board_backend.dev.ed.task_board_backend.entity.TaskStatus;
import task_board_backend.dev.ed.task_board_backend.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void shouldReturnAllTasksForBoard() throws Exception {
        when(taskService.findByBoard(1L)).thenReturn(List.of(
                new TaskDto(1L, "Task 1", "Desc 1", TaskStatus.TODO, LocalDate.of(2024, 12, 31)),
                new TaskDto(2L, "Task 2", "Desc 2", TaskStatus.IN_PROGRESS, null)
        ));

        mockMvc.perform(get("/api/boards/1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[0].status").value("TODO"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Task 2"))
                .andExpect(jsonPath("$[1].status").value("IN_PROGRESS"));
    }

    @Test
    void shouldReturn404WhenBoardNotFoundOnGetTasks() throws Exception {
        when(taskService.findByBoard(999L))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Board nicht gefunden: 999"));

        mockMvc.perform(get("/api/boards/999/tasks"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Board nicht gefunden: 999"));
    }

    @Test
    void shouldCreateTask() throws Exception {
        when(taskService.create(eq(1L), any(CreateTaskRequest.class)))
                .thenReturn(new TaskDto(1L, "New Task", "Description", TaskStatus.TODO, LocalDate.of(2024, 12, 31)));

        mockMvc.perform(post("/api/boards/1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Task\",\"description\":\"Description\",\"dueDate\":\"2024-12-31\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(header().string("Location", "/api/boards/1/tasks/1"));
    }

    @Test
    void shouldCreateTaskWithMinimalFields() throws Exception {
        when(taskService.create(eq(1L), any(CreateTaskRequest.class)))
                .thenReturn(new TaskDto(1L, "Minimal Task", null, TaskStatus.TODO, null));

        mockMvc.perform(post("/api/boards/1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Minimal Task\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Minimal Task"));
    }

    @Test
    void shouldReturn400WhenCreatingTaskWithEmptyTitle() throws Exception {
        mockMvc.perform(post("/api/boards/1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validierung fehlgeschlagen"))
                .andExpect(jsonPath("$.details[0]").value("title: Titel darf nicht leer sein"));
    }

    @Test
    void shouldReturn404WhenCreatingTaskForNonExistentBoard() throws Exception {
        when(taskService.create(eq(999L), any(CreateTaskRequest.class)))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Board nicht gefunden: 999"));

        mockMvc.perform(post("/api/boards/999/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Task\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Board nicht gefunden: 999"));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        doNothing().when(taskService).delete(1L, 1L);

        mockMvc.perform(delete("/api/boards/1/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService).delete(1L, 1L);
    }

    @Test
    void shouldReturn404WhenDeletingFromNonExistentBoard() throws Exception {
        doThrow(new jakarta.persistence.EntityNotFoundException("Board nicht gefunden: 999"))
                .when(taskService).delete(999L, 1L);

        mockMvc.perform(delete("/api/boards/999/tasks/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Board nicht gefunden: 999"));
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentTask() throws Exception {
        doThrow(new jakarta.persistence.EntityNotFoundException("Task nicht gefunden: 999"))
                .when(taskService).delete(1L, 999L);

        mockMvc.perform(delete("/api/boards/1/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Task nicht gefunden: 999"));
    }

    @Test
    void shouldUpdateTaskStatus() throws Exception {
        when(taskService.updateStatus(eq(1L), eq(1L), any(UpdateTaskStatusRequest.class)))
                .thenReturn(new TaskDto(1L, "Task", "Desc", TaskStatus.IN_PROGRESS, null));

        mockMvc.perform(patch("/api/boards/1/tasks/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void shouldReturn400WhenUpdatingStatusWithNullStatus() throws Exception {
        mockMvc.perform(patch("/api/boards/1/tasks/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validierung fehlgeschlagen"))
                .andExpect(jsonPath("$.details[0]").value("status: Status darf nicht null sein"));
    }

    @Test
    void shouldReturn404WhenUpdatingStatusForNonExistentBoard() throws Exception {
        when(taskService.updateStatus(eq(999L), eq(1L), any(UpdateTaskStatusRequest.class)))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Board nicht gefunden: 999"));

        mockMvc.perform(patch("/api/boards/999/tasks/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"DONE\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Board nicht gefunden: 999"));
    }

    @Test
    void shouldReturn404WhenUpdatingStatusForNonExistentTask() throws Exception {
        when(taskService.updateStatus(eq(1L), eq(999L), any(UpdateTaskStatusRequest.class)))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Task nicht gefunden: 999"));

        mockMvc.perform(patch("/api/boards/1/tasks/999/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"DONE\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Task nicht gefunden: 999"));
    }
}