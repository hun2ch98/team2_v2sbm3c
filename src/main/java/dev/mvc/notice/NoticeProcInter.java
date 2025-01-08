package dev.mvc.notice;

import java.util.ArrayList;

public interface NoticeProcInter {
  /**
   * 공지사항 등록
   */
  public int create(NoticeVO noticeVO);
  
  /**
   * 전체 목록
   * @return
   */
  public ArrayList<NoticeVO> list_all();
  
  /** 조회 */
  public NoticeVO read(int noticeno);
  
  /** 추천수 증가 */
  public int increaseGoodcnt(int noticeno);
  
  /** 추천수 감소 */
  public int decreaseGoodcnt(int noticeno);
  
  /** 추천 */
  public int good(int noticeno);
  
  /** 조회수 증가 */
  public int increaseCnt(int noticeno);
  
  /** 글 수정 */
  public int update(NoticeVO noticeVO);
  
  /** 삭제 */
  public int delete(int noticeno);
}
