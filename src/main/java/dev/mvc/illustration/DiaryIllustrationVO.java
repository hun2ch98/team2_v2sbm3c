package dev.mvc.illustration;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class DiaryIllustrationVO {

  /** 일기 번호, Sequence를 통해 자동 생성 */
  private Integer diaryno;
  
  /** 일기 날짜  */
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date ddate;
  
  /** 날씨 테이블과 연결 */
  private int weatherno;
  
  /** 날씨 이미지 */
  private String we_file1 = "";
  
  /** 감정번호 */
  private int emono;
  
  /** 감정 이미지 */
  private String em_file1 = "";
  
  /** 그림 번호 */
  private int illustno;
  
  /** 메인 이미지 preview */
  private String illust_thumb = "";
  
  
  
  
  
  
  
  
  
  
  
  
  
  
}
