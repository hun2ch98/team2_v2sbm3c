package dev.mvc.diarygood;

import java.util.ArrayList;
import java.util.HashMap;

public interface DiaryGoodProcInter {

  
  public int create(DiaryGoodVO diaryGoodVO);
  
  
  public ArrayList<DiaryGoodVO> list_all();
  
  
  public int delete(int goodno);
  

  public DiaryGoodVO read(int goodno);
  
  
  public DiaryGoodVO read(HashMap<String, Object> map);
  
  
  public int heartCnt(HashMap<String, Object> map);
}
