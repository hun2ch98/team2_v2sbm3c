package dev.mvc.reply;

public interface ReplyDAOInter {
  /**
   * 댓글 추가
   * @param replyVO
   * @return
   */
  public int create(ReplyVO replyVO);

  /**
   * 댓글 조회
   * @param replyno
   * @return
   */
  public ReplyVO read(int replyno);
  
/**
 * 댓글 수정
 * @param replyVO
 * @return
 */
  public int update(ReplyVO replyVO);

  /**
   * 댓글 삭제
   * @param replyno
   * @return
   */
  public int delete(int replyno) ;
}
