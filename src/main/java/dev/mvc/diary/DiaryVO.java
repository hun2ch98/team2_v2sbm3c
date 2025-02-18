package dev.mvc.diary;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import dev.mvc.illustration.IllustrationVO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE diary(
//DIARYNO                            NUMBER(10)     NOT NULL     PRIMARY KEY,
//TITLE                            VARCHAR2(20)  NOT NULL,  
//DDATE                             VARCHAR2(30)  NOT NULL,
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
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date ddate;
  
  /** 날씨 테이블과 연결 */
  private int weatherno;
  
  /** 감정번호 */
  private int emono;
  
  /** 요약일기  */
  private String summary;
  
  /** 회원번호 */
  private int memberno;

  private int cnt=0;
  
  private int goodcnt=0;
  
  /** 자동 저장을 위한 image 변수 */
  private String illust_file="";
  
  private ArrayList<IllustrationVO> get_illust;
}
