package dev.mvc.diarygood;

import java.util.ArrayList;
import java.util.HashMap;

public interface DiaryGoodDAOInter {

  public int create(DiaryGoodVO diaryGoodVO);
  
  
  public ArrayList<DiaryGoodVO> list_all();
  
  
  public int delete(int goodno);
  
  
  public DiaryGoodVO read(int goodno);
  
  
  public DiaryGoodVO readByDiaryMember(HashMap<String, Object> map);

  /**
   * 특정 일기의 특정 회원 좋아요 수 산출
   * @param map
   * @return
   */
  public int heartCnt(HashMap<String, Object> map);
}
