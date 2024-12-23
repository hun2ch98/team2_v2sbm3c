package dev.mvc.survey;

import java.util.ArrayList;

public interface SurveyProcInter {
  
  /**
   * 등록
   * @param surveyVO
   * @return
   */
  public int create(SurveyVO surveyVO);
  
  /**
   * 전체 목록
   * @return
   */
  public ArrayList<SurveyVO> list_all();
  
  /**
   * 조회
   * @param surveyno
   * @return
   */
  public SurveyVO read(int surveyno);
  
  /**
   * 글 수정
   * @param surveyVO
   * @return
   */
  public int update_text(SurveyVO surveyVO);
  
  /**
   * 파일 수정
   * @param surveyVO
   * @return
   */
  public int update_file(SurveyVO surveyVO);
  
  /**
   * 삭제
   * @param surveyno
   * @return
   */
  public int delete(int surveyno);

}
