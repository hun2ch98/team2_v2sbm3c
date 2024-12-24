package dev.mvc.grade;

import java.util.List;

public interface GradeDAOInter {
  public int create(GradeVO gradeVO); // 등급 생성
  
  public List<GradeVO> list();        // 등급 목록 조회
  
  public GradeVO read(int gradeno);   // 등급 상세 조회
  
  public int update(GradeVO gradeVO); // 등급 수정
  
  public int delete(int gradeno);     // 등급 삭제
}
