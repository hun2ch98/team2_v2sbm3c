package dev.mvc.learningdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class LearningdataVO {
	
//	CREATE TABLE learningdata (
//			datano	    NUMBER(100)		    NOT NULL,
//			ethical	    VARCHAR(100)	    NOT	NULL,
//			create_at	DATE		NOT NULL,
//			ques			VARCHAR(100)		NOT NULL,
//			ans			VARCHAR(100)		NOT NULL
//		);
	
  /**학습 데이터 번호*/
  private int datano;
  
  /**도덕성 검증 여부*/
  private String ethical;
  
  /**데이터 등록일*/
  private String create_at = "";
  
  /**학습 데이터 질문*/
  private String ques ="";
  
  /**학습 데이터 답변*/
  private String ans ="";
  
  /**회원 번호*/
  private int memberno;

}
