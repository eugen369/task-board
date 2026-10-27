package task_board_backend.dev.ed.task_board_backend.repository;

import task_board_backend.dev.ed.task_board_backend.entity.Board;
import task_board_backend.dev.ed.task_board_backend.entity.Task;
import task_board_backend.dev.ed.task_board_backend.entity.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void shouldSaveAndFindTaskByBoardId() {
        Board board = entityManager.persistAndFlush(new Board("Test Board"));

        Task task1 = new Task("Task 1", board);
        Task task2 = new Task("Task 2", board);
        entityManager.persistAndFlush(task1);
        entityManager.persistAndFlush(task2);

        Board otherBoard = entityManager.persistAndFlush(new Board("Other Board"));
        entityManager.persistAndFlush(new Task("Other Task", otherBoard));

        List<Task> tasks = taskRepository.findByBoardId(board.getId());

        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(Task::getTitle).containsExactlyInAnyOrder("Task 1", "Task 2");
    }

    @Test
    void shouldReturnEmptyListWhenBoardHasNoTasks() {
        Board board = entityManager.persistAndFlush(new Board("Empty Board"));

        List<Task> tasks = taskRepository.findByBoardId(board.getId());

        assertThat(tasks).isEmpty();
    }

    @Test
    void shouldSaveTaskWithAllFields() {
        Board board = entityManager.persistAndFlush(new Board("Test Board"));

        Task task = new Task("Full Task", board);
        task.setDescription("Description");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setDueDate(LocalDate.of(2024, 12, 31));

        Task saved = entityManager.persistAndFlush(task);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Full Task");
        assertThat(saved.getDescription()).isEqualTo("Description");
        assertThat(saved.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(saved.getDueDate()).isEqualTo(LocalDate.of(2024, 12, 31));
        assertThat(saved.getBoard()).isEqualTo(board);
    }

    @Test
    void shouldDefaultStatusToTodo() {
        Board board = entityManager.persistAndFlush(new Board("Test Board"));

        Task task = new Task("Default Status", board);
        Task saved = entityManager.persistAndFlush(task);

        assertThat(saved.getStatus()).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void shouldDeleteTask() {
        Board board = entityManager.persistAndFlush(new Board("Test Board"));
        Task task = entityManager.persistAndFlush(new Task("To Delete", board));

        taskRepository.deleteById(task.getId());

        assertThat(taskRepository.findById(task.getId())).isEmpty();
    }

    @Test
    void shouldCascadeDeleteTasksWhenBoardDeleted() {
        Board board = entityManager.persistAndFlush(new Board("Board with Tasks"));
        entityManager.persistAndFlush(new Task("Task 1", board));
        entityManager.persistAndFlush(new Task("Task 2", board));

        entityManager.remove(board);
        entityManager.flush();

        assertThat(taskRepository.findByBoardId(board.getId())).isEmpty();
    }

    @Test
    void shouldSupportOptimisticLocking() {
        Board board = entityManager.persistAndFlush(new Board("Test Board"));
        Task task = entityManager.persistAndFlush(new Task("Lock Test", board));

        Long originalVersion = task.getVersion();

        task.setTitle("Updated");
        entityManager.persistAndFlush(task);

        assertThat(task.getVersion()).isGreaterThan(originalVersion);
    }
}