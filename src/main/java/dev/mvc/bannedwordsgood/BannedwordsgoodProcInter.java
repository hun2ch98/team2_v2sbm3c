package dev.mvc.bannedwordsgood;

import java.util.ArrayList;
import java.util.HashMap;

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
  
  /**
   * 특정 금지단어의 특정 회원 추천 갯수 산출
   * @param map
   * @return
   */
  public int heartCnt(HashMap<String, Object> map);
  
  /**
   * 조회
   * @param wordno
   * @return
   */
  public BannedwordsgoodVO read(int goodno);
  
  /**
   * wordno, memberno로 조회
   * @param wordno
   * @return
   */
  public BannedwordsgoodVO readByWordnoMemeberno(HashMap<String, Object> map);
  
  /**
   * 테이블 3개 join
   * @return
   */
  public ArrayList<BannedwordsBannedwordsgoodMemberVO> list_all_join();
  
}
