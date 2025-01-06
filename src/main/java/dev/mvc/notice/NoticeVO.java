package dev.mvc.notice;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE notice(
//    noticeno    NUMBER(10)                  NOT NULL PRIMARY KEY,
//    memberno    NUMBER(10)                  NOT NULL,
//    title       VARCHAR(300)                NOT NULL,
//    content     VARCHAR(4000)               NOT NULL,
//    cnt         NUMBER(7)                   NOT NULL,
//    rdate       DATE                        NOT NULL,
//    FOREIGN KEY (memberno)  REFERENCES member (memberno)
//);

@Setter @Getter @ToString
public class NoticeVO {
  
  /** 공지사항 번호 */
  private int noticeno;
  
  /** 관리자 권한의 회원 번호 */
  private int memberno;
  
  /** 공지사항 제목 */
  private String title;
  
  /** 공지사항 내용 */
  private String content;
  
  /** 조회수 */
  private int cnt = 0;
  
  /** 등록 날짜 */
  private String rdate = "";
}
