package dev.mvc.board;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class BoardVO {
  /*
   * CREATE TABLE board(
    boardno         NUMBER(10)      NOT NULL    PRIMARY KEY,
    memberno        NUMBER(10)      NOT NULL,
    bcontent        CLOB            NOT NULL,
    rdate           DATE            NOT NULL,
    board_cate      NUMBER(10)      NOT NULL,
    file1           VARCHAR(200)        NULL,
    file1saved      VARCHAR2(100)    NULL,
    thumb1          VARCHAR2(100)    NULL,
    size1           NUMBER(10)         NULL,
    FOREIGN KEY (memberno)  REFERENCES member (memberno)
    );
   */

  /** 게시판 번호*/
  private int boardno;
  
  /** 작성 멤버 */
  private Integer memberno;
  
  /** 게시글 제목*/
  @NotEmpty(message="제목 입력은 필수 사항입니다.")
  @Size(min=1, max=50)
  private String title="";
  
  /** 게시글 내용*/
  @NotEmpty(message="내용 입력은 필수 사항입니다.")
  private String bcontent ="";
  
  /** 작성 날짜*/
  private String rdate = "";
  
  /** 게시글 종류 */
  private int board_cate;
  
  //파일 업로드 관련
  // -----------------------------------------------------------------------------------
  /**
  이미지 파일
  <input type='file' class="form-control" name='file1MF' id='file1MF' 
             value='' placeholder="파일 선택">
  */
  private MultipartFile file1MF = null;
  /** 메인 이미지 크기 단위, 파일 크기 */
  private String size1_label = "";
  /** 메인 이미지 */
  private String file1 = "";
  /** 실제 저장된 메인 이미지 */
  private String file1saved = "";
  /** 메인 이미지 preview */
  private String thumb1 = "";
  /** 메인 이미지 크기 */
  private long size1 = 0;

}