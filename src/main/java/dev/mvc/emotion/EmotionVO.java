package dev.mvc.emotion;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE emotion (
//    	emono	NUMBER(10)		    NOT NULL,
//		type	VARCHAR(50)		    NOT NULL,
//		explan  VARCHAR(500)        NOT NULL,
//		img	    VARCHAR(500)		NULL
//  );

@Getter @Setter @ToString
public class EmotionVO {
  
  /** 감정 번호 */
  private int emono;
  
  /** 일기 번호*/
  private int diaryno;
  
  /** 감정 종류 */
  private String type="";
  
  /** 감정 설명*/
  private String explan="";
  
  /** 감정 이미지 */
  private String img="";

}
