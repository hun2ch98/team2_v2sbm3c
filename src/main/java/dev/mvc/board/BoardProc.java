package dev.mvc.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dev.mvc.board.BoardProc")
public class BoardProc implements BoardProcInter {
  @Autowired
  private BoardDAOInter boardDAO;
  
  public BoardProc() {
    System.out.println("-> Board created.");
  }
  
  @Override
  public int create(BoardVO boardVO) {
    int cnt = this.boardDAO.create(boardVO);
    
    return cnt;
  }
  
  @Override
  public BoardVO read(int boardno) {
    BoardVO boardVO = this.boardDAO.read(boardno);
    
    return boardVO;
  }
  
  @Override
  public int update(BoardVO boardVO) {
    int cnt = this.boardDAO.update(boardVO);
    
    return cnt;
  }

  @Override
  public int delete(int boardno) {
      int cnt = this.boardDAO.delete(boardno);
      return cnt;
  }


}
