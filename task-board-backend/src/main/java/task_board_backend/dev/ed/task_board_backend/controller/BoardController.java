package task_board_backend.dev.ed.task_board_backend.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import task_board_backend.dev.ed.task_board_backend.dto.BoardDto;
import task_board_backend.dev.ed.task_board_backend.dto.CreateBoardRequest;
import task_board_backend.dev.ed.task_board_backend.service.BoardService;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public List<BoardDto> getAll() {
        return boardService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boardService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<BoardDto> create(@Valid @RequestBody CreateBoardRequest request) {
        BoardDto created = boardService.create(request);
        return ResponseEntity.created(URI.create("/api/boards" + created.id())).body(created);
    }

}
