package dev.mvc.notice;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.dto.SearchDTO;
import dev.mvc.tool.Security;

@Component("dev.mvc.notice.NoticeProc")
public class NoticeProc implements NoticeProcInter{

  @Autowired
  private NoticeDAOInter noticeDAO;
  
  /** 공지사항 등록 */
  @Override
  public int create(NoticeVO noticeVO) {
    int cnt = this.noticeDAO.create(noticeVO);
    return cnt;
  }
  
  /** 공지사항 전체 목록 */
  @Override
  public ArrayList<NoticeVO> list_all() {
    ArrayList<NoticeVO> list = this.noticeDAO.list_all();
    
    return list;
  }
  
  /** 조회 */
  @Override
  public NoticeVO read(int noticeno) {
    NoticeVO noticeVO = this.noticeDAO.read(noticeno);
    return noticeVO;
  }
  
  /** 추천수 증가 */
  @Override
  public int increaseGoodcnt(int noticeno) {
    int cnt = this.noticeDAO.increaseGoodcnt(noticeno);
    return cnt;
  }
  
  /** 추천수 감소 */
  @Override
  public int decreaseGoodcnt(int noticeno) {
    int cnt = this.noticeDAO.decreaseGoodcnt(noticeno);
    return cnt;
  }
  
  /** 추천 */
  @Override
  public int good(int noticeno) {
    
    return 0;
  }
  
  /** 조회수 증가 */
  @Override
  public int increaseCnt(int noticeno) {
    int cnt = this.noticeDAO.increaseCnt(noticeno);
    return cnt;
  }
  
  /** 글 수정 */
  @Override
  public int update(NoticeVO noticeVO) {
    int cnt = this.noticeDAO.update(noticeVO);
    return cnt;
  }
  
  /** 글 삭제 */
  @Override
  public int delete(int noticeVO) {
    int cnt = this.noticeDAO.delete(noticeVO);
    return cnt;
  }
  
  /** 조건에 맞는 공지사항 수 */
  @Override
  public int list_search_count(SearchDTO searchDTO) {
    return noticeDAO.list_search_count(searchDTO);
  }
  
  /** 공지사항 검색 + 목록 페이징 */
  @Override
  public ArrayList<NoticeVO> list_search_paging(SearchDTO searchDTO) {
    return noticeDAO.list_search_paging(searchDTO);
  }
}
