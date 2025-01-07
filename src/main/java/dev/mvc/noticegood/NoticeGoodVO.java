package dev.mvc.noticegood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE noticegood (
//    noticegoodno      NUMBER(10)  NOT NULL    PRIMARY KEY,
//    rdate             DATE      NULL,
//    memberno          NUMBER(10)  NOT NULL,
//      noticeno            NUMBER(10)  NOT NULL,
//      FOREIGN KEY (memberno)  REFERENCES member (memberno),
//      FOREIGN KEY (noticeno)  REFERENCES notice (noticeno)
//  );

@Getter @Setter @ToString
public class NoticeGoodVO {
  
  /** 공지사항 추천 번호 */
  private int noticegoodno;
  
  /** 등록일 */
  private String rdate = "";
  
  /** 회원 번호 */
  private int memberno;
  
  /** 공지사항 번호 */
  private int noticeno;
  
}
