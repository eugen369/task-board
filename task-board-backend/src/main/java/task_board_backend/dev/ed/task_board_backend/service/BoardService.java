package task_board_backend.dev.ed.task_board_backend.service;

import task_board_backend.dev.ed.task_board_backend.dto.BoardDto;
import task_board_backend.dev.ed.task_board_backend.dto.CreateBoardRequest;
import task_board_backend.dev.ed.task_board_backend.entity.Board;
import task_board_backend.dev.ed.task_board_backend.repository.BoardRepository;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<BoardDto> findAll() {
        return boardRepository.findAll().stream()
                .map(b -> new BoardDto(b.getId(), b.getName()))
                .toList();
    }

    public BoardDto findById(Long id) {
        return boardRepository.findById(id)
                .map(b -> new BoardDto(b.getId(), b.getName()))
                .orElseThrow(() -> new EntityNotFoundException("Board nicht gefunden: " + id));
    }

    public void delete(Long id) {
        if (!boardRepository.existsById(id)) {
            throw new EntityNotFoundException("Board nicht gefunden: " + id);
        }
        boardRepository.deleteById(id);
    }

    public BoardDto create(CreateBoardRequest request) {
        Board board = new Board(request.name());
        Board saved = boardRepository.save(board);
        return new BoardDto(saved.getId(), saved.getName());
    }

}
