package dev.mvc.bannedwordsgood;

import java.util.ArrayList;

import dev.mvc.bannedwords.BannedwordsVO;

public interface BannedwordsgoodProcInter {
	
 /**
   * 등록, 추상 메소드
   * @param bannedwordsgoodVO
   * @return
   */
  public int create(BannedwordsgoodVO bannedwordsgoodVO);
	  
  /**
   * 전체 목록
   * @return
   */
  public ArrayList<BannedwordsgoodVO> list_all();

}
