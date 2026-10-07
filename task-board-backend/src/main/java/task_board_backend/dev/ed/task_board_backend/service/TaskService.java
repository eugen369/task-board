package task_board_backend.dev.ed.task_board_backend.service;

import task_board_backend.dev.ed.task_board_backend.dto.CreateTaskRequest;
import task_board_backend.dev.ed.task_board_backend.dto.TaskDto;
import task_board_backend.dev.ed.task_board_backend.dto.UpdateTaskStatusRequest;
import task_board_backend.dev.ed.task_board_backend.entity.Board;
import task_board_backend.dev.ed.task_board_backend.entity.Task;
import task_board_backend.dev.ed.task_board_backend.repository.BoardRepository;
import task_board_backend.dev.ed.task_board_backend.repository.TaskRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final BoardRepository boardRepository;

    public TaskService(TaskRepository taskRepository, BoardRepository boardRepository) {
        this.taskRepository = taskRepository;
        this.boardRepository = boardRepository;
    }

    public List<TaskDto> findByBoard(Long boardId) {

        if (!boardRepository.existsById(boardId)) {
            throw new EntityNotFoundException("Board nicht gefunden: " + boardId);
        }

        return taskRepository.findByBoardId(boardId).stream()
                .map(this::toDto)
                .toList();
    }

    public TaskDto create(Long boardId, CreateTaskRequest request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board nicht gefunden: " + boardId));

        Task task = new Task(request.title(), board);
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());

        Task saved = taskRepository.save(task);
        return toDto(saved);
    }

    public void delete(Long boardId, Long taskId) {
        if (!boardRepository.existsById(boardId)) {
            throw new EntityNotFoundException("Board nicht gefunden: " + boardId);
        }
        if (!taskRepository.existsById(taskId)) {
            throw new EntityNotFoundException("Task nicht gefunden: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }

    public TaskDto updateStatus(Long boardId, Long taskId, UpdateTaskStatusRequest request) {
        if (!boardRepository.existsById(boardId)) {
            throw new EntityNotFoundException("Board nicht gefunden: " + boardId);
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task nicht gefunden: " + taskId));

        task.setStatus(request.status());
        return toDto(taskRepository.save(task));
    }

    private TaskDto toDto(Task task) {
        return new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getDueDate());
    }

}
