package projectManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectManagement.entities.Board;
import projectManagement.entities.User;
import projectManagement.repository.BoardRepo;

import java.util.Optional;

@Service
public class BoardService {
    @Autowired
    BoardRepo boardRepo;

    public BoardService() {
    }

    public Optional<Board> getBoard(long boardId) {
        return boardRepo.findById(boardId);
    }

}
