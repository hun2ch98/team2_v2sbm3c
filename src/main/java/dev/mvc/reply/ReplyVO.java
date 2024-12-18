package dev.mvc.reply;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ReplyVO {
  /*
   * replyno    NUMBER(10)    NOT NULL,
   * rcontent   VARCHAR(100)  NOT NULL,
   * rdate      DATE          NOT NULL
  댓글 */
  
  /* 댓글 번호 */
  private int replyno;
  /** 게시판 번호*/
  private int boardno;
  /* 댓글 내용 */
  private String rcontent;
  /* 작성 날짜 */
  private String rdate;
  /** 패스워드 */
  private String passwd = "";
  /** 추천수 */
  private int recom;
  /** 관리자 권한의 회원 번호 */
  private int memberno;
}
