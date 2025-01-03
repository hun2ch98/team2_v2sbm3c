package dev.mvc.schedule;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE schedule (
//scheduleno  NUMBER(10)    NOT NULL  PRIMARY KEY, -- AUTO_INCREMENT 대체
//sdate   VARCHAR2(10)  NOT NULL, -- 출력할 날짜 2013-10-20
//label       VARCHAR2(50)  NOT NULL, -- 달력에 출력될 레이블
//title       VARCHAR2(100) NOT NULL, -- 제목(*)
//content     CLOB          NOT NULL, -- 글 내용
//seqno       NUMBER(5)     DEFAULT 1     NOT NULL, -- 일정 출력 순서
//regdate     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 등록 날짜
//memberno    NUMBER(10)     NOT NULL, -- FK
//FOREIGN KEY (memberno) REFERENCES member (memberno) -- 일정을 등록한 관리자
//);

@Getter @Setter @ToString
public class ScheduleVO {
  /** 일정 번호 */
  private int scheduleno;
  
  /** 회원 번호 */
  private int memberno;
  
  /** 출력할 날짜 */
  private String sdate = "";
  
  /** 출력할 레이블 */
  private String label = "";
  
  /** 제목 */
  private String title = "";
  
  /** 글 내용 */
  private String content = "";
  
  /** 일정 출력 순서 */
  private int seqno;
  
  /** 등록 날짜 */
  private String regdate = "";
}
