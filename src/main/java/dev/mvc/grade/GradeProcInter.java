package dev.mvc.grade;

import java.util.List;

public interface GradeProcInter {
  /**
   * 등급 생성
   * @param gradeVO
   * @return
   */
  public int create(GradeVO gradeVO);
  
  /**
   * 등급 목록 조회
   * @return
   */
  public List<GradeVO> list();
  
  /**
   * 등급 상세 조회
   * @param gradeno
   * @return
   */
  public GradeVO read(int gradeno);
  
  /**
   * 등급 수정
   * @param gradeVO
   * @return
   */
  public int update(GradeVO gradeVO);
  
  /**
   * 등급 삭제
   * @param gradeno
   * @return
   */
  public int delete(int gradeno);
}
