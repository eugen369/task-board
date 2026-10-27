package task_board_backend.dev.ed.task_board_backend.controller;

import task_board_backend.dev.ed.task_board_backend.dto.BoardDto;
import task_board_backend.dev.ed.task_board_backend.dto.CreateBoardRequest;
import task_board_backend.dev.ed.task_board_backend.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BoardController.class)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BoardService boardService;

    @Test
    void shouldReturnAllBoards() throws Exception {
        when(boardService.findAll()).thenReturn(List.of(
                new BoardDto(1L, "Board 1"),
                new BoardDto(2L, "Board 2")
        ));

        mockMvc.perform(get("/api/boards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Board 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Board 2"));
    }

    @Test
    void shouldReturnBoardById() throws Exception {
        when(boardService.findById(1L)).thenReturn(new BoardDto(1L, "Test Board"));

        mockMvc.perform(get("/api/boards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Board"));
    }

    @Test
    void shouldReturn404WhenBoardNotFound() throws Exception {
        when(boardService.findById(999L))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Board nicht gefunden: 999"));

        mockMvc.perform(get("/api/boards/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Board nicht gefunden: 999"));
    }

    @Test
    void shouldCreateBoard() throws Exception {
        when(boardService.create(any(CreateBoardRequest.class)))
                .thenReturn(new BoardDto(1L, "New Board"));

        mockMvc.perform(post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Board\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Board"))
                .andExpect(header().string("Location", "/api/boards1"));
    }

    @Test
    void shouldReturn400WhenCreatingBoardWithEmptyName() throws Exception {
        mockMvc.perform(post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validierung fehlgeschlagen"))
                .andExpect(jsonPath("$.details[0]").value("name: Name darf nicht leer sein"));
    }

    @Test
    void shouldDeleteBoard() throws Exception {
        doNothing().when(boardService).delete(1L);

        mockMvc.perform(delete("/api/boards/1"))
                .andExpect(status().isNoContent());

        verify(boardService).delete(1L);
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentBoard() throws Exception {
        doThrow(new jakarta.persistence.EntityNotFoundException("Board nicht gefunden: 999"))
                .when(boardService).delete(999L);

        mockMvc.perform(delete("/api/boards/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Board nicht gefunden: 999"));
    }
}