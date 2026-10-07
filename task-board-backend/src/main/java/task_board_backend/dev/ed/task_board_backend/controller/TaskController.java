package task_board_backend.dev.ed.task_board_backend.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import task_board_backend.dev.ed.task_board_backend.dto.CreateTaskRequest;
import task_board_backend.dev.ed.task_board_backend.dto.TaskDto;
import task_board_backend.dev.ed.task_board_backend.dto.UpdateTaskStatusRequest;
import task_board_backend.dev.ed.task_board_backend.service.TaskService;

@RestController
@RequestMapping("/api/boards/{boardId}/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDto> getAll(@PathVariable Long boardId) {
        return taskService.findByBoard(boardId);
    }

    @PostMapping
    public ResponseEntity<TaskDto> create(@PathVariable Long boardId, @Valid @RequestBody CreateTaskRequest request) {
        TaskDto created = taskService.create(boardId, request);
        return ResponseEntity.created(URI.create("/api/boards/" + boardId + "/tasks/" + created.id())).body(created);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long boardId,
            @PathVariable Long taskId) {
        taskService.delete(boardId, taskId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskDto> updateStatus(
            @PathVariable Long boardId,
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskStatusRequest request) {
        return ResponseEntity.ok(taskService.updateStatus(boardId, taskId, request));
    }

}
