package dev.mvc.loginlog;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class LoginlogVO {
//  CREATE TABLE loginlog (
//      loginlogno   NUMBER(10)                     NOT NULL    PRIMARY KEY,
//        id           VARCHAR(30)                    NOT NULL,
//        ip           VARCHAR(15)                    NOT NULL,
//        result       VARCHAR(1)      DEFAULT 'F'    NOT NULL,
//        ldate        DATE                           NOT NULL
//    );
  
  /** 로그인 로그 번호 */
  private int loginlogno = 0;
  
  /** 로그인 시도 id */
  private String id = "";
  
  /** 로그인 시도 ip */
  private String ip = "";
  
  /** 로그인 성공 여부 */
  private String result = "F";
  
  /** 로그인 시도 날짜 */
  private String ldate = "";
}
