package dev.mvc.grade;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeVO {
  /*
  CREATE TABLE grade (
      gradeno         NUMBER(5)       NOT NULL    PRIMARY KEY,   -- 등급 번호 
      memberno        NUMBER(5)       NULL,                      -- 회원 번호
      grade_name      VARCHAR2(20)    NOT NULL,                  -- 등급 이름
      gdescription    VARCHAR2(255)   NULL,                      -- 등급 설명
      min_points      NUMBER(10)      NOT NULL,                  -- 최소 포인트
      max_points      NUMBER(10)      NOT NULL,                  -- 최대 포인트
      rdate           DATE            NOT NULL,                  -- 생성 날짜
      img_url         VARCHAR2(255)   NULL,                      -- 이미지 경로
      FOREIGN KEY (memberno) REFERENCES member(memberno)
  );
  */
  
  private int gradeno;           // 등급 번호
  
  private int memberno;          // 회원 번호
  
  private String grade_name;     // 등급 이름
  
  private String gdescription;   // 등급 설명
  
  private int min_points;        // 최소 포인트
  
  private int max_points;        // 최대 포인트
  
  private String created_at;     // 생성 날짜
  
  private String img_url;        // 이미지 경로
}
