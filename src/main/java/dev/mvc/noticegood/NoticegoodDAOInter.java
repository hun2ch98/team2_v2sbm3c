package dev.mvc.noticegood;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.notice.NoticeVO;

public interface NoticegoodDAOInter {
  /**
   * 등록, 추상 메서드
   * @param noticegoodVO
   * @return
   */
  public int create(NoticegoodVO noticegoodVO);
  
  /**
   * 전체 목록
   * @return
   */
  public ArrayList<NoticegoodVO> list_all();
  
  /** 삭제 */
  public int delete(int noticegoodno);
  
  /**
   * 특정 공지사항의 특정 회원 추천 갯수 산출
   * @param map
   * @return
   */
  public int heart_Cnt(HashMap<String, Object> map);
  
  /**
   * 조회
   */
  public NoticegoodVO read(int noticegoodno);
  
  
  /**
   * noticeno, memberno로 조회
   */
  public NoticegoodVO readByNoticeMember(HashMap<String, Object> map);
  
  /**
   * 테이블 3개 join
   * @return
   */
  public ArrayList<NoticeNoticegoodMemberVO> list_all_join();
  
}
