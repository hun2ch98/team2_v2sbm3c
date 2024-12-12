package dev.mvc.emotion;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE emotion (
//    emno      NUMBER(10)    NOT NULL    PRIMARY KEY,
//    em_type     VARCHAR(100)    NOT NULL,
//    em_image  VARCHAR(100)  NOT NULL
//  );

@Getter @Setter @ToString
public class EmotionVO {
  
  /** 감정 번호 */
  private int emno;
  
  /** 감정 종류 */
  private String em_type="";
  
  /** 감정 이미지 */
  private String em_image="";

}
