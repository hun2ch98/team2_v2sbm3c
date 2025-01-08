package dev.mvc.diarygood;

import java.util.ArrayList;
import java.util.HashMap;

public interface DiaryGoodProcInter {

  
  public int create(DiaryGoodVO diaryGoodVO);
  
  
  public ArrayList<DiaryGoodVO> list_all();
  
  
  public int delete(int goodno);

  
  public int heartCnt(HashMap<String, Object> map);
}
