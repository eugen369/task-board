package task_board_backend.dev.ed.task_board_backend.service;

import task_board_backend.dev.ed.task_board_backend.dto.CreateTaskRequest;
import task_board_backend.dev.ed.task_board_backend.dto.TaskDto;
import task_board_backend.dev.ed.task_board_backend.dto.UpdateTaskStatusRequest;
import task_board_backend.dev.ed.task_board_backend.entity.Board;
import task_board_backend.dev.ed.task_board_backend.entity.Task;
import task_board_backend.dev.ed.task_board_backend.entity.TaskStatus;
import task_board_backend.dev.ed.task_board_backend.repository.BoardRepository;
import task_board_backend.dev.ed.task_board_backend.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class TaskServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private BoardRepository boardRepository;

    private TaskService taskService;

    @Autowired
    void setTaskService(TaskRepository taskRepository, BoardRepository boardRepository) {
        this.taskService = new TaskService(taskRepository, boardRepository);
    }

    @Test
    void shouldFindTasksByBoard() {
        Board board = entityManager.persistAndFlush(new Board("Test Board"));
        entityManager.persistAndFlush(new Task("Task 1", board));
        entityManager.persistAndFlush(new Task("Task 2", board));

        Board otherBoard = entityManager.persistAndFlush(new Board("Other Board"));
        entityManager.persistAndFlush(new Task("Other Task", otherBoard));

        List<TaskDto> tasks = taskService.findByBoard(board.getId());

        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(TaskDto::title).containsExactlyInAnyOrder("Task 1", "Task 2");
    }

    @Test
    void shouldThrowWhenBoardNotFoundOnFind() {
        assertThatThrownBy(() -> taskService.findByBoard(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Board nicht gefunden: 999");
    }

    @Test
    void shouldCreateTask() {
        Board board = entityManager.persistAndFlush(new Board("Test Board"));
        CreateTaskRequest request = new CreateTaskRequest("New Task", "Description", LocalDate.of(2024, 12, 31));

        TaskDto created = taskService.create(board.getId(), request);

        assertThat(created.id()).isNotNull();
        assertThat(created.title()).isEqualTo("New Task");
        assertThat(created.description()).isEqualTo("Description");
        assertThat(created.status()).isEqualTo(TaskStatus.TODO);
        assertThat(created.dueDate()).isEqualTo(LocalDate.of(2024, 12, 31));
    }

    @Test
    void shouldCreateTaskWithMinimalFields() {
        Board board = entityManager.persistAndFlush(new Board("Test Board"));
        CreateTaskRequest request = new CreateTaskRequest("Minimal Task", null, null);

        TaskDto created = taskService.create(board.getId(), request);

        assertThat(created.title()).isEqualTo("Minimal Task");
        assertThat(created.description()).isNull();
        assertThat(created.dueDate()).isNull();
        assertThat(created.status()).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void shouldThrowWhenCreatingTaskForNonExistentBoard() {
        CreateTaskRequest request = new CreateTaskRequest("Task", "Desc", null);

        assertThatThrownBy(() -> taskService.create(999L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Board nicht gefunden: 999");
    }

    @Test
    void shouldDeleteTask() {
        Board board = entityManager.persistAndFlush(new Board("Test Board"));
        Task task = entityManager.persistAndFlush(new Task("To Delete", board));

        taskService.delete(board.getId(), task.getId());

        assertThat(taskRepository.findById(task.getId())).isEmpty();
    }

    @Test
    void shouldThrowWhenDeletingFromNonExistentBoard() {
        Board board = entityManager.persistAndFlush(new Board("Test Board"));
        Task task = entityManager.persistAndFlush(new Task("Task", board));

        assertThatThrownBy(() -> taskService.delete(999L, task.getId()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Board nicht gefunden: 999");
    }

    @Test
    void shouldThrowWhenDeletingNonExistentTask() {
        Board board = entityManager.persistAndFlush(new Board("Test Board"));

        assertThatThrownBy(() -> taskService.delete(board.getId(), 999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Task nicht gefunden: 999");
    }

    @Test
    void shouldUpdateTaskStatus() {
        Board board = entityManager.persistAndFlush(new Board("Test Board"));
        Task task = entityManager.persistAndFlush(new Task("Status Task", board));
        assertThat(task.getStatus()).isEqualTo(TaskStatus.TODO);

        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest(TaskStatus.IN_PROGRESS);
        TaskDto updated = taskService.updateStatus(board.getId(), task.getId(), request);

        assertThat(updated.status()).isEqualTo(TaskStatus.IN_PROGRESS);

        Task reloaded = entityManager.find(Task.class, task.getId());
        assertThat(reloaded.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldUpdateTaskStatusToDone() {
        Board board = entityManager.persistAndFlush(new Board("Test Board"));
        Task task = entityManager.persistAndFlush(new Task("Done Task", board));

        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest(TaskStatus.DONE);
        TaskDto updated = taskService.updateStatus(board.getId(), task.getId(), request);

        assertThat(updated.status()).isEqualTo(TaskStatus.DONE);
    }

    @Test
    void shouldThrowWhenUpdatingStatusForNonExistentBoard() {
        Board board = entityManager.persistAndFlush(new Board("Test Board"));
        Task task = entityManager.persistAndFlush(new Task("Task", board));

        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest(TaskStatus.DONE);

        assertThatThrownBy(() -> taskService.updateStatus(999L, task.getId(), request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Board nicht gefunden: 999");
    }

    @Test
    void shouldThrowWhenUpdatingStatusForNonExistentTask() {
        Board board = entityManager.persistAndFlush(new Board("Test Board"));

        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest(TaskStatus.DONE);

        assertThatThrownBy(() -> taskService.updateStatus(board.getId(), 999L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Task nicht gefunden: 999");
    }
}