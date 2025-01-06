package dev.mvc.surveyitem;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ItemVO {
  
  /**
   * CREATE TABLE surveyitem (
    itemno      NUMBER(10)      NOT NULL,
    surveyno    NUMBER(10)      NOT NULL,
    item_seq    NUMBER(5)       NOT NULL,   항목 출력 순서
    item        VARCHAR2(200)   NOT NULL,   항목
    item_cnt    NUMBER(7)           NULL,   항목 선택 인원
    FOREIGN KEY (surveyno) REFERENCES survey (surveyno)
    );
   */
  
  /** 항목 번호 */
  private int itemno;
  
  /** 설문조사 번호 */
  private int surveyno;
  
  /** 항목 출력 순서 */
  private int item_seq;
  
  /** 항목 */
  private String item="";
  
  /** 항목 선택 인원 */
  private int item_cnt=0;

}
