package task_board_backend.dev.ed.task_board_backend.repository;

import task_board_backend.dev.ed.task_board_backend.entity.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    void shouldSaveAndFindBoardById() {
        Board board = new Board("Test Board");
        Board saved = entityManager.persistAndFlush(board);

        Optional<Board> found = boardRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Board");
    }

    @Test
    void shouldReturnEmptyWhenBoardNotFound() {
        Optional<Board> found = boardRepository.findById(999L);

        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindAllBoards() {
        entityManager.persistAndFlush(new Board("Board 1"));
        entityManager.persistAndFlush(new Board("Board 2"));
        entityManager.persistAndFlush(new Board("Board 3"));

        var boards = boardRepository.findAll();

        assertThat(boards).hasSize(3);
    }

    @Test
    void shouldDeleteBoard() {
        Board board = entityManager.persistAndFlush(new Board("To Delete"));

        boardRepository.deleteById(board.getId());

        assertThat(boardRepository.findById(board.getId())).isEmpty();
    }

    @Test
    void shouldCheckExistsById() {
        Board board = entityManager.persistAndFlush(new Board("Exists"));

        boolean exists = boardRepository.existsById(board.getId());
        boolean notExists = boardRepository.existsById(999L);

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}