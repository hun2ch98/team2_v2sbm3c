package dev.mvc.learningdata;

public class LearningdataVO {
  /*
  datano      NUMBER(100)       NOT NULL, --학습 데이터 번호
  data_type VARCHAR(100)      NOT NULL, --데이터 타입
  ethical     CHAR(1)             DEFAULT 'Y' NOT NULL, --도덕성 검증 여부
  rdate     DATE              NOT NULL, --데이터 등록일
  content     VARCHAR(100)      NOT NULL --학습 데이터 내용
  */
  
  /**학습 데이터 번호*/
  private int datano;
  /**데이터 타입*/
  private String data_type ="";
  /**데이터 등록일*/
  private String rdate = "";
  /**학습 데이터 내용*/
  private String content ="";
  
}
