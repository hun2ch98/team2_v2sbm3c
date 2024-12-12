package dev.mvc.aiconversation;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE aiconversation (
//    conversationno  NUMBER(10)   NOT NULL PRIMARY KEY,
//    content         VARCHAR(255) NOT NULL,
//    rdate           DATE         NOT NULL,
//    memberno        NUMBER(10)   NOT NULL,
//    summary         VARCHAR(255) NOT NULL,
//    FOREIGN KEY (memberno) REFERENCES member (memberno)
//);

@Getter @Setter @ToString
public class AiconversationVO {
  
  /** 대화 번호 */
  private Integer conversationno;
  
  /** 회원 번호 */
  private Integer memberno;
  
  /** 대화 내용 */
  @NotEmpty(message="내용을 필수 사항입니다.")
  @Size(min=1, max=255)
  private String content="";
  
  /** 대화 날짜 */
  private String rdate="";
 
  /** ai 생성 일기 */
  private String summary="";

}
