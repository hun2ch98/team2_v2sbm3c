package dev.mvc.diarygood;

import java.util.ArrayList;

public interface DiaryGoodDAOInter {

  public int create(DiaryGoodVO diaryGoodVO);
  
  
  public ArrayList<DiaryGoodVO> list_all();
  
  
  public int delete(int goodno);
}
