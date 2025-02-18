package dev.mvc.survey;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SurveyVO {
  
/**
 * CREATE TABLE survey (
  surveyno      NUMBER(10)    NOT NULL,
  memberno        NUMBER(10)      NOT NULL,
  topic         VARCHAR(100)  NOT NULL,
  sdate         VARCHAR(10)       NULL,
  edate         VARCHAR(10)       NULL,
  s_number      NUMBER(7)   NOT NULL,
  is_continue     CHAR(1)       NOT NULL,
  poster          VARCHAR(100)    NULL,
  poster_saved  VARCHAR(100)    NULL,
  poster_size     NUMBER(10)        NULL,
  poster_thumb  VARCHAR(100)    NULL,
  FOREIGN KEY (memberno)  REFERENCES member (memberno)
  );
 */
  
  /** 설문조사 번호 */
  private int surveyno;
  
  /** 회원 번호 */
  private int memberno;
  
  /** 설문조사 주제 */
  @NotEmpty(message="주제 입력은 필수 사항입니다.")
  @Size(min=1, max=100)
  private String topic = "";
  
  public SurveyVO() {
    
  }
  
  public SurveyVO(int surveyno, String topic) {
    this.surveyno = surveyno;
    this.topic = topic;
  }
  
  /** 시작 날짜 */
  private String sdate="";
  
  /** 종료 날짜 */
  private String edate="";
  
  /** 참여 인원 */
  @NotNull(message="참여 인원는 필수 입력 항목입니다.")
  private int s_number;
  
  /** 진행 여부 */
  @NotEmpty(message="진행 여부는 필수 항목입니다.")
  @Pattern(regexp="^[YN]$", message="Y 또는 N만 입력 가능합니다.")
  private String is_continue;
  
  /** 추천 */
  private int goodcnt;
  
//파일 업로드 관련
  // -----------------------------------------------------------------------------------
  /**
  이미지 파일
  <input type='file' class="form-control" name='file1MF' id='file1MF' 
             value='' placeholder="파일 선택">
  */
  private MultipartFile file1MF = null;
  /** 메인 이미지 크기 단위, 파일 크기 */
  private String size1_label = "";
  /** 메인 이미지 */
  private String file1 = "";
  /** 실제 저장된 메인 이미지 */
  private String file1saved = "";
  /** 메인 이미지 preview */
  private String thumb1 = "";
  /** 메인 이미지 크기 */
  private long size1 = 0;

  
}
