package dev.mvc.reply;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ReplyVO {
  /*
   * replyno    NUMBER(10)    NOT NULL,
   * rcontent   VARCHAR(100)  NOT NULL,
   * rdate      DATE          NOT NULL,
   * fixed_at  DATE         NOT NULL
   */
  
  /* 댓글 번호 */
  private int replyno;
  /* 댓글 내용 */
  private String rcontent;
  /* 작성 날짜 */
  private String rdate;
  /*수정 시간*/
  private String fixed_at;
  
}
