package dev.mvc.noticegood;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.noticegood.NoticegoodProc")
public class NoticegoodProc implements NoticegoodProcInter {
  @Autowired
  NoticegoodDAOInter noticegoodDAO;
  
  @Override
  public int create(NoticegoodVO noticegoodVO) {
    int cnt = this.noticegoodDAO.create(noticegoodVO);
    return cnt;
  }

}
