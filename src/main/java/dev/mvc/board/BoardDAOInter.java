package dev.mvc.board;

import java.util.ArrayList;

public interface BoardDAOInter {
  
  /**
   * 게시글 추가
   * @param boardVO
   * @return
   */
  public int create(BoardVO boardVO);
  
  /**
   * 게시글 조회
   * @param boardno
   * @return
   */
  public BoardVO read(int boardno);
  
  /**
   * 모든 목록
   * @return
   */
  public ArrayList<BoardVO> list_all();
  
  /**
   * 게시글 등록된 목록
   * @param boardno
   * @return
   */
  public ArrayList<BoardVO> list_by_boardno(int boardno);
  
  /**
   * 게시글 내용 수정
   * @param boardVO
   * @return
   */
  public int update_text(BoardVO boardVO);
  
  /**
   * 파일 수정
   * @param boardVO
   * @return
   */
  public int update_file(BoardVO boardVO);
  
  /**
   * 게시글 삭제
   * @param boardno
   * @return
   */
  public int delete(int boardno);

}
