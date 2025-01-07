package dev.mvc.diarygood;

import java.util.ArrayList;

public interface DiaryGoodProcInter {

  
  public int create(DiaryGoodVO diaryGoodVO);
  
  
  public ArrayList<DiaryGoodVO> list_all();
  
  
  public int delete(int goodno);
}
