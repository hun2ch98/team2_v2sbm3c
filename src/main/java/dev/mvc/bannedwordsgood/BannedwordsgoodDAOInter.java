package dev.mvc.bannedwordsgood;

import java.util.ArrayList;
import java.util.HashMap;


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
  
  /**
   * 금지 단어 삭제
   * @param wordno
   * @return
   */
  public int delete(int goodno);
  
  /**
   * 특정 금지단어의 특정 회원 추천 갯수 산출
   * @param map
   * @return
   */
  public int heartCnt(HashMap<String, Object> map);
  
}
