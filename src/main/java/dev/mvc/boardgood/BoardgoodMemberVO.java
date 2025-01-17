package dev.mvc.boardgood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class BoardgoodMemberVO {
  
  /**
   * CREATE TABLE boardgood (
      goodno        NUMBER(10) NOT NULL PRIMARY KEY, -- AUTO_INCREMENT 대체
      boardno      NUMBER(10)         NOT NULL,
      bdate         DATE          NOT NULL, -- 등록 날짜
      memberno      NUMBER(10)     NOT NULL , -- FK
      FOREIGN KEY (memberno) REFERENCES member (memberno),-- 일정을 등록한 관리자 
      FOREIGN KEY (boardno) REFERENCES board (boardno) ON DELETE CASCADE
    );
   */
  
  /** 추천 번호 */
  public int goodno;
  
  /** 요청사항 번호 */
  public int boardno;
  
  /** 요청사항 제목 */
  private String b_title="";
  
  /** 회원 번호 */
  public int memberno;
  
  /** 등록 날짜 */
  public String bdate;
  
  /** 회원 이름 */
  private String name = "";
  
  /** 이메일 주소 */
  private String email="";

}
