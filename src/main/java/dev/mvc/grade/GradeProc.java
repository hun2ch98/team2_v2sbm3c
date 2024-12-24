package dev.mvc.grade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.grade.GradeProc")
public class GradeProc implements GradeProcInter{
  
  @Autowired
  private GradeDAOInter gradeDAO;
  
  @Override
  public int create(GradeVO gradeVO) {
    return gradeDAO.create(gradeVO);
  }
  
  @Override
  public List<GradeVO> list() {
    return gradeDAO.list();
  }
  
  @Override
  public GradeVO read(int gradeno) {
    return gradeDAO.read(gradeno);
  }
  
  @Override
  public int update(GradeVO gradeVO) {
    return gradeDAO.update(gradeVO);
  }
  
  @Override
  public int delete(int gradeno) {
    return gradeDAO.delete(gradeno);
  }
  
}
