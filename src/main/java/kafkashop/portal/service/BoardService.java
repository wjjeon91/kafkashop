package kafkashop.portal.service;

import kafkashop.portal.domain.Board;
import kafkashop.portal.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public void saveBoard(Board board) {
        boardRepository.save(board);
    }

    public Board findById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }
}
