package task_board_backend.dev.ed.task_board_backend.repository;

import task_board_backend.dev.ed.task_board_backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByBoardId(Long boardId);
}
