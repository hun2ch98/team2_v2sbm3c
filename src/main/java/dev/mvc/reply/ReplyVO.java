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
   * rdate      DATE          NOT NULL,
   * fixed_at  DATE         NOT NULL
   */
  
  /* 댓글 번호 */
  private int replyno;
  /* 댓글 내용 */
  private String rcontent;
  /* 작성 날짜 */
  private String rdate;
<<<<<<< HEAD
  /*수정 시간*/
  private String fixed_at;
=======
  /** 패스워드 */
  private String passwd = "";
  /** 추천수 */
  private int recom;
  /** 관리자 권한의 회원 번호 */
  private int memberno;
    
  }
>>>>>>> upstream/main
  
}
