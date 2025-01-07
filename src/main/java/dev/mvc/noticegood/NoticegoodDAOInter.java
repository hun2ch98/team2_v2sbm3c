package dev.mvc.noticegood;

import java.util.ArrayList;

public interface NoticegoodDAOInter {
  /**
   * 등록, 추상 메서드
   * @param noticegoodVO
   * @return
   */
  public int create(NoticegoodVO noticegoodVO);
  
  /**
   * 전체 목록
   * @return
   */
  public ArrayList<NoticegoodVO> list_all();
}
