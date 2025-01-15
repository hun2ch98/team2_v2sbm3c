package dev.mvc.weather;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.board.BoardVO;

public interface WeatherDAOInter {
  
  /**
   * 감정 생성
   * @param WeatherVO
   * @return
   */
  public int create(WeatherVO weatherVO);
  
  /**
   * 감정 조회
   * @param emno
   * @return
   */
  public WeatherVO read(int weatherno);
  
  /**
   * 모든 목록
   * @return
   */
  public ArrayList<WeatherVO> list_all();

  public ArrayList<WeatherVO> image_list();
  
  /**
   * 감정 등록된 목록
   * @param emono
   * @return
   */
  public ArrayList<WeatherVO> list_by_weatherno(int weatherno);
  
  /**
   * 감정 내용 수정
   * @param WeatherVO
   * @return
   */
  public int update_text(WeatherVO weatherVO);
  
  /**
   * 파일 수정
   * @param WeatherVO
   * @return
   */
  public int update_file(WeatherVO weatherVO);
  
  /**
   * 게시글 삭제
   * @param emono
   * @return
   */
  public int delete(int weatherno);
  
  /**
   * 게시글 종류별 검색 목록
   * @param hashMap
   * @return
   */
  public ArrayList<WeatherVO> list_by_weatherno_search(HashMap<String, Object> hashMap);
  
  /**
   * 게시글 종류별 검색 레코드 갯수
   * @param hashMap
   * @return
   */
  public int count_by_weatherno_search(HashMap<String, Object> hashMap);
  
  /**
   * 게시글 종류별 검색 및 페이징
   * @param map
   * @return
   */
  public ArrayList<WeatherVO> list_by_weatherno_search_paging(HashMap<String, Object> map);
}
