package dev.mvc.board;

public interface BoardProcInter {
  
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
   * 게시글 내용 수정
   * @param boardVO
   * @return
   */
  public int update(BoardVO boardVO);
  
  /**
   * 게시글 삭제
   * @param boardno
   * @return
   */
  public int delete(int boardno);


}
