package dev.mvc.diarygood;

import java.util.ArrayList;
import java.util.HashMap;

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

  @Override
  public ArrayList<DiaryGoodVO> list_all() {
    ArrayList<DiaryGoodVO> list = this.diaryGoodDAO.list_all();
    return list;
  }

  @Override
  public int delete(int goodno) {
    int cnt = this.diaryGoodDAO.delete(goodno);
    return cnt;
  }
  
  
  @Override
  public DiaryGoodVO read(int goodno) {
    DiaryGoodVO diaryGoodVO = this.diaryGoodDAO.read(goodno);
    return diaryGoodVO;
  }
  
  
  @Override
  public DiaryGoodVO readByDiaryMember(HashMap<String, Object> map) {
    DiaryGoodVO diaryGoodVO = this.diaryGoodDAO.readByDiaryMember(map);
    return diaryGoodVO;
  }

  @Override
  public int heartCnt(HashMap<String, Object> map) {
    int cnt = this.diaryGoodDAO.heartCnt(map);
    return cnt;
  }
  
}
