package dev.mvc.participants;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class PartVO {

  /**
   * CREATE TABLE participants(
    pno                NUMBER(10)   NOT NULL    PRIMARY KEY,
    pdate              DATE         NOT NULL,
    itemno             NUMBER(10)       NULL ,
    memberno           NUMBER(6)        NULL ,
    FOREIGN KEY (itemno) REFERENCES surveyitem (itemno),
    FOREIGN KEY (memberno) REFERENCES MEMBER (memberno)
    );
   */
  
  /** 설문조사 참여 회원 번호 */
  public int pno;
  
  /** 설문조사 항목 번호 */
  public int itemno;
  
  /** 회원 번호 */
  public int memberno;
  
  /** 참여일 */
  public String pdate="";
}
