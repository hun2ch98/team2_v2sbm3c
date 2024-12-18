package dev.mvc.board;

public class BoardVO {
  /*
   * CREATE TABLE board(
    boardno         NUMBER(10)      NOT NULL    PRIMARY KEY,
    memberno        NUMBER(10)      NOT NULL,
    bcontent        CLOB            NOT NULL,
    rdate           DATE            NOT NULL,
    board_cate      NUMBER(10)      NOT NULL,
    file1           VARCHAR(200)        NULL,
    FOREIGN KEY (memberno)  REFERENCES member (memberno)
    );
   */

  /** 게시판 번호*/
  private int boardno;
  
  /** 작성 멤버 */
  private Integer memberno;
  
  /** 게시글 내용*/
  private String bcontent ="";
  
  /** 작성 날짜*/
  private String rdate = "";
  
  /** 게시글 종류 */
  private int board_cate;
  
  /** 파일 업로드 */
  private String file1="";


}
