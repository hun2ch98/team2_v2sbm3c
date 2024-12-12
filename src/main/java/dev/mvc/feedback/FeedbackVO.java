package dev.mvc.feedback;

public class FeedbackVO {
  /*
   * "feedbackno" NUMBER(100)   NOT NULL, --피드백 번호
  "fcontent"  VARCHAR(1000)   NOT NULL, --피드백 내용
  "rdate" DATE    NOT NULL, -- 작성 날짜
  "memberno"  NUMBER(100)   NOT NULL --회원 번호 FK
   */

  /**피드백 번호*/
  private int feedbackno;
  /**피드백 내용*/
  private String fcontent ="";
  /**작성 날짜*/
  private String rdate = "";
}
