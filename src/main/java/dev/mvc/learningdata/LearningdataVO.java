package dev.mvc.learningdata;

public class LearningdataVO {
  /*
  datano      NUMBER(100)       NOT NULL, --학습 데이터 번호
  ethical     CHAR(1)             DEFAULT 'Y' NOT NULL, --도덕성 검증 여부
  ques        VARCHAR(100)        NOT NULL, -- 학습 데이터 질문
  ans     VARCHAR(100)      NOT NULL --학습 데이터 답변
  */
  
  /**학습 데이터 번호*/
  private int datano;
  /**데이터 등록일*/
  private String rdate = "";
  /**학습 데이터 질문*/
  private String ques ="";
  /**학습 데이터 답변*/
  private String ans ="";
}
