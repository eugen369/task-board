package task_board_backend.dev.ed.task_board_backend.repository;

import task_board_backend.dev.ed.task_board_backend.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
