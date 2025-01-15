package dev.mvc.notice;

import java.util.ArrayList;

import dev.mvc.dto.SearchDTO;
import dev.mvc.member.MemberVO;

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
  
  /**
   * 검색 공지사항 수
   * @param noticeVO
   * @return
   */
  public int list_search_count(SearchDTO searchDTO);
  
  /**
   * 공지사항 검색 + 페이징 목록
   * @param noticeVO
   * @return
   */
  public ArrayList<NoticeVO> list_search_paging(SearchDTO searchDTO);
}
