package dev.mvc.participants;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ItemMemberPartVO {

  /**
   * -- 테이블 3개 join, as 사용시 컬럼명 변경 가능: c.title as n_title
        SELECT p.pno, p.itemno, p.memberno, p.pdate, i.item as i_item, m.memberno, m.email, m.name
        FROM surveyitem i, participants p, member m
        WHERE i.itemno = p.itemno AND p.memberno = m.memberno
        ORDER BY pno DESC; 
   */
  
  /** 설문조사 참여 회원 번호 */
  public int pno;
  
  /** 설문조사 항목 번호 */
  public int itemno;
  
  /** 회원 번호 */
  public int memberno;
  
  /** 참여일 */
  public String pdate="";
  
  /** 항목 */
  private String i_item="";
  
  /** 이름 */
  private String name = "";
  
  /** 이메일 주소 */
  private String email="";

}
