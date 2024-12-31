package dev.mvc.diary;

import java.util.Date;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE diary(
//DIARYNO                            NUMBER(10)     NOT NULL     PRIMARY KEY,
//TITLE                            VARCHAR2(20)  NOT NULL,  
//DATE                             VARCHAR2(30)  NOT NULL,
//WEATHER_CODE                           CHAR(1)      DEFAULT 'N'    NOT NULL,
//SUMMARY                             NUMBER(5)     DEFAULT 1     NOT NULL
//);

@Getter @Setter @ToString
public class DiaryVO {
  
  /** 일기 번호, Sequence를 통해 자동 생성 */
  private Integer diaryno;
  
  /** 일기 제목 */
  @NotEmpty(message="제목은 필수 사항입니다.")
  @Size(min=1, max=20)
  private String title="";
  
  /** 일기 날짜  */
  private Date ddate;
  
  /** 날씨 테이블과 연결 */
  private int weatherno;
  
  /** 감정번호 */
  private int emno;
  
  /** 요약일기  */
  private String summary;
  
  /** 회원번호 */
  private int memberno;
  
  /** 그림번호 */
  private int illustno;

  
}
