package dev.mvc.noticegood;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.notice.NoticeVO;

@Component("dev.mvc.noticegood.NoticegoodProc")
public class NoticegoodProc implements NoticegoodProcInter {
  @Autowired
  NoticegoodDAOInter noticegoodDAO;
  
  /** 등록, 추상 메서드 */
  @Override
  public int create(NoticegoodVO noticegoodVO) {
    int cnt = this.noticegoodDAO.create(noticegoodVO);
    return cnt;
  }
  
  /** 전체 목록 */
  @Override
  public ArrayList<NoticegoodVO> list_all() {
    ArrayList<NoticegoodVO> list = this.noticegoodDAO.list_all();
    
    return list;
  }
  
  /** 부모 레코드 noticegoodno 삭제 */
  @Override
  public int delete(int noticegoodVO) {
    int cnt = this.noticegoodDAO.delete(noticegoodVO);
    return cnt;
  }
  
  /** 자식 레코드 noticeno 삭제 */
  @Override
  public int n_delete(int noticeno) {
    int cnt = this.noticegoodDAO.n_delete(noticeno);
    return cnt;
  }
  
  @Override
  public int heart_Cnt(HashMap<String, Object> map) {
    int cnt = this.noticegoodDAO.heart_Cnt(map);
    return cnt;
  }
  
  /** 조회 */
  @Override
  public NoticegoodVO read(int noticegoodno) {
    NoticegoodVO noticegoodVO = this.noticegoodDAO.read(noticegoodno);
    return noticegoodVO;
  }
  
  @Override
  public NoticegoodVO readByNoticeMember(HashMap<String, Object> map) {
    NoticegoodVO noticegoodVO = this.noticegoodDAO.readByNoticeMember(map);
    return noticegoodVO;
  }
  
  @Override
  public ArrayList<NoticeNoticegoodMemberVO> list_all_join() {
    ArrayList<NoticeNoticegoodMemberVO> list = this.noticegoodDAO.list_all_join();
    return list;
  }

}
