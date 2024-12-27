package dev.mvc.emotion;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.board.BoardVO;

public interface EmotionDAOInter {
  
  /**
   * 감정 생성
   * @param emotionVO
   * @return
   */
  public int create(EmotionVO emotionVO);
  
  /**
   * 감정 조회
   * @param emno
   * @return
   */
  public EmotionVO read(int emono);
  
  /**
   * 모든 목록
   * @return
   */
  public ArrayList<EmotionVO> list_all();
  
  /**
   * 감정 등록된 목록
   * @param emono
   * @return
   */
  public ArrayList<EmotionVO> list_by_emono(int emono);
  
  /**
   * 감정 내용 수정
   * @param emotionVO
   * @return
   */
  public int update_text(EmotionVO emotionVO);
  
  /**
   * 파일 수정
   * @param emotionVO
   * @return
   */
  public int update_file(EmotionVO emotionVO);
  
  /**
   * 게시글 삭제
   * @param emono
   * @return
   */
  public int delete(int emono);
  
  /**
   * 게시글 종류별 검색 목록
   * @param hashMap
   * @return
   */
  public ArrayList<EmotionVO> list_by_emono_search(HashMap<String, Object> hashMap);
  
  /**
   * 게시글 종류별 검색 레코드 갯수
   * @param hashMap
   * @return
   */
  public int count_by_emono_search(HashMap<String, Object> hashMap);
  
  /**
   * 게시글 종류별 검색 및 페이징
   * @param map
   * @return
   */
  public ArrayList<EmotionVO> list_by_emono_search_paging(HashMap<String, Object> map);
}
