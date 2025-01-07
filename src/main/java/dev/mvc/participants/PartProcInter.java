package dev.mvc.participants;

import java.util.ArrayList;

public interface PartProcInter {
  
  /**
   * 등록
   * @param partVO
   * @return
   */
  public int create(PartVO partVO);
  
  /**
   * 목록
   * @return
   */
  public ArrayList<PartVO> list_all();
  
  /**
   * 삭제
   * @param pno
   * @return
   */
  public int delete(int pno);

}
