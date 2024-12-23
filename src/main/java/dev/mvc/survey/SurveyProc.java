package dev.mvc.survey;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dev.mvc.survey.SurveyProc")
public class SurveyProc implements SurveyProcInter {
  @Autowired
  private SurveyDAOInter surveyDAO;
  
  public SurveyProc() {
    System.out.println("-> SurveyProc created.");
  }
  
  @Override
  public int create(SurveyVO surveyVO) {
    int cnt = this.surveyDAO.create(surveyVO);
    return cnt;
  }
  
  @Override
  public ArrayList<SurveyVO> list_all(){
    ArrayList<SurveyVO> list = this.surveyDAO.list_all();
    return list;
  }
  
  @Override
  public SurveyVO read(int surveyno) {
    SurveyVO surveyVO = this.surveyDAO.read(surveyno);
    return surveyVO;
  }
  
  @Override
  public int update_text(SurveyVO surveyVO) {
    int cnt = this.surveyDAO.update_text(surveyVO);
    return cnt;
  }
  
  @Override
  public int update_file(SurveyVO surveyVO) {
    int cnt = this.surveyDAO.update_file(surveyVO);
    return cnt;
  }
  
  @Override
  public int delete(int surveyno) {
    int cnt = this.surveyDAO.delete(surveyno);
    return cnt;
  }

}
