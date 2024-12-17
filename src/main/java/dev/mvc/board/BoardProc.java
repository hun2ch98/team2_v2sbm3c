package dev.mvc.board;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Security;
import dev.mvc.tool.Tool;

@Component("dev.mvc.board.BoardProc")
public class BoardProc implements BoardProcInter {
  @Autowired
  Security security;
  
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
  public ArrayList<BoardVO> list_all() {
    ArrayList<BoardVO> list = this.boardDAO.list_all();
    return list;
  }
  
  @Override
  public int update_text(BoardVO boardVO) {
    int cnt = this.boardDAO.update_text(boardVO);
    return cnt;
  }
  
  @Override
  public int update_file(BoardVO boardVO) {
    int cnt = this.boardDAO.update_file(boardVO);
    return cnt;
  }

  @Override
  public int delete(int boardno) {
      int cnt = this.boardDAO.delete(boardno);
      return cnt;
  }



}
