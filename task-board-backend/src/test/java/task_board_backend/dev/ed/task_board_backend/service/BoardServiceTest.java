package task_board_backend.dev.ed.task_board_backend.service;

import task_board_backend.dev.ed.task_board_backend.dto.BoardDto;
import task_board_backend.dev.ed.task_board_backend.dto.CreateBoardRequest;
import task_board_backend.dev.ed.task_board_backend.entity.Board;
import task_board_backend.dev.ed.task_board_backend.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import jakarta.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class BoardServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BoardRepository boardRepository;

    private BoardService boardService;

    @Autowired
    void setBoardService(BoardRepository boardRepository) {
        this.boardService = new BoardService(boardRepository);
    }

    @Test
    void shouldCreateBoard() {
        CreateBoardRequest request = new CreateBoardRequest("New Board");

        BoardDto created = boardService.create(request);

        assertThat(created.id()).isNotNull();
        assertThat(created.name()).isEqualTo("New Board");
    }

    @Test
    void shouldFindAllBoards() {
        entityManager.persistAndFlush(new Board("Board 1"));
        entityManager.persistAndFlush(new Board("Board 2"));

        var boards = boardService.findAll();

        assertThat(boards).hasSize(2);
        assertThat(boards).extracting(BoardDto::name).containsExactlyInAnyOrder("Board 1", "Board 2");
    }

    @Test
    void shouldFindBoardById() {
        Board board = entityManager.persistAndFlush(new Board("Find Me"));

        BoardDto found = boardService.findById(board.getId());

        assertThat(found.id()).isEqualTo(board.getId());
        assertThat(found.name()).isEqualTo("Find Me");
    }

    @Test
    void shouldThrowWhenBoardNotFound() {
        assertThatThrownBy(() -> boardService.findById(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Board nicht gefunden: 999");
    }

    @Test
    void shouldDeleteBoard() {
        Board board = entityManager.persistAndFlush(new Board("To Delete"));

        boardService.delete(board.getId());

        assertThat(boardRepository.findById(board.getId())).isEmpty();
    }

    @Test
    void shouldThrowWhenDeletingNonExistentBoard() {
        assertThatThrownBy(() -> boardService.delete(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Board nicht gefunden: 999");
    }
}