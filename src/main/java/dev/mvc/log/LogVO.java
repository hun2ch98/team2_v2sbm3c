package dev.mvc.log;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class LogVO {
/*
   * CREATE TABLE log(
   	logno         NUMBER(10)      NOT NULL    PRIMARY KEY,
    memberno      NUMBER(10)      NOT NULL,
    table_name         VARCHAR(50)     NOT NULL,
    action        VARCHAR(50)     NOT NULL,
    ldate         VARCHAR(10)        NULL,
    details       VARCHAR2(50)    NULL,
    ip    VARCHAR(50),
    FOREIGN KEY (memberno)  REFERENCES member (memberno)
    );
   */

  /** 로그 번호*/
  private int logno;
  
  /** 사용자 번호 */
  private int memberno;
  
  /** 영향을 받은 테이블 */
  private String table_name="";
  
  /** 수행된 기능 */
  private String action ="";
  
  /** 기능 사용 시간 */
  private String ldate = "";
  
  /** 세부정보 */
  private String details = "";
  
  /** ip 주소 */
  private String ip = "";
  
  private String is_success="";
  

}