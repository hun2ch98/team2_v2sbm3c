package dev.mvc.diarygood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class DiaryGoodVO {
  /**
   * CREATE TABLE diarygood (
  goodno  NUMBER(10) NOT NULL PRIMARY KEY, -- AUTO_INCREMENT 대체
  diaryno NUMBER(10)         NOT NULL,
  rdate     DATE          NOT NULL, -- 등록 날짜
  memberno    NUMBER(10)     NOT NULL , -- FK
  FOREIGN KEY (memberno) REFERENCES member (memberno),-- 일정을 등록한 관리자 
  FOREIGN KEY (diaryno) REFERENCES diary (diaryno)
  );
   */
  
  /** 추천 번호 */
  public int goodno;
  
  /** 컨텐츠 번호 */
  public int diaryno;
  
  /** 관리자 권한의 회원 번호 */
  public int memberno;
  
  /** 등록 날짜 */
  public String rdate;

}
