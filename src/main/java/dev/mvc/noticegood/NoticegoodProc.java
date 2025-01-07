package dev.mvc.noticegood;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

}
