package dev.mvc.weather;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE emotion (
//weatherno	NUMBER(10)		    NOT NULL,
//type	VARCHAR(50)		    NOT NULL,
//explan  VARCHAR(500)        NOT NULL,
//file1           VARCHAR(200)        NULL,
//file1saved      VARCHAR2(100)		 NULL,
//thumb1          VARCHAR2(100)		 NULL,
//size1           NUMBER(10)		     NULL
//);

@Getter @Setter @ToString
public class WeatherVO {
  
  /** 감정 번호 */
  private int weatherno;
  
  /** 일기 번호*/
  private int diaryno;
  
  /** 회원 번호*/
  private int memberno;
  
  /** 감정 종류 */
  @NotEmpty(message="종류 입력은 필수 사항입니다.")
  private String type="";
  
  /** 감정 설명*/
  @NotEmpty(message="설명 입력은 필수 사항입니다.")
  private String explan="";
  
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
