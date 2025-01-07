package dev.mvc.diarygood;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.diarygood.DiaryGoodProc")
public class DiaryGoodProc implements DiaryGoodProcInter {
  
  @Autowired // ContentsDAOInter interface를 구현한 클래스의 객체를 만들어 자동으로 할당해라.
  private DiaryGoodDAOInter diaryGoodDAO;
  
  public int create(DiaryGoodVO diaryGoodVO) {
    int cnt = this.diaryGoodDAO.create(diaryGoodVO);
    return cnt;
  }
  
}
