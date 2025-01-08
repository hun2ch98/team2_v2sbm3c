package dev.mvc.surveygood;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SurveySurveygoodMemberVO {

  /**
   *  -- 3개 조인
      SELECT g.goodno, g.rdate, g.surveyno as s_topic, g.memberno, m.id, m.email
      FROM survey s, surveygood g, member m
      WHERE s.surveyno = g.surveyno AND g.surveyno = m.memberno
      ORDER BY goodno DESC;
   */
  
  /** 추천 번호 */
  public int goodno;
  
  /** 설문조사 번호 */
  public int surveyno;
  
  /** 등록 날짜 */
  public String rdate;
  
  /** 설문조사 주제 */
  private String s_topic = "";
  
  /** 회원 번호 */
  public int memberno;
  
  /** 아이디(이메일) */
  private String id = "";
  
  /** 이메일 주소 */
  private String email="";
  
}
