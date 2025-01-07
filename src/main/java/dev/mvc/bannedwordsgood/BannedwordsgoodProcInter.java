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
  
  /**
   * 삭제
   * @param wordno 삭제할 레코드 PK
   * @return 삭제된 레코드 갯수
   */
  public int delete(int goodno);

}
