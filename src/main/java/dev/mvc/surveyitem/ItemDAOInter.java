package dev.mvc.surveyitem;

import java.util.ArrayList;

public interface ItemDAOInter {
  
  /**
   * 등록
   * @param itemVO
   * @return
   */
  public int create(ItemVO itemVO);
  
  /**
   * 조회
   * @param itemno
   * @return
   */
  public ItemVO read(int itemno);
  
  /**
   * 회원
   * 목록
   * @return
   */
  public ArrayList<ItemVO> list_all_com(int surveyno);
  
  /**
   * 관리자
   * 목록
   * @return
   */
  public ArrayList<ItemVO> admin_list(int surveyno);
  
  /**
   * 수정
   * @param itemVO
   * @return
   */
  public int update(ItemVO itemVO);
  
  /**
   * 삭제
   * @param itemVO
   * @return
   */
  public int delete(int itemVO);
  
  /**
   * 회원 참여수
   * @param itemVO
   * @return
   */
  public int update_cnt(int itemVO);

}
