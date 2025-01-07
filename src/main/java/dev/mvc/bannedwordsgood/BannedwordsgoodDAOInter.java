package dev.mvc.bannedwordsgood;

import java.util.ArrayList;


public interface BannedwordsgoodDAOInter {
  /**
   * 등록, 추상 메소드
   * @param contentsgoodVO
   * @return
   */
  public int create(BannedwordsgoodVO bannedwordsgoodVO);
  
  /**
   * 모든 목록
   * @return
   */
  public ArrayList<BannedwordsgoodVO> list_all();
}
