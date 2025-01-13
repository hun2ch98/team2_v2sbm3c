package dev.mvc.noticegood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//-- 테이블 3개 join, as 사용시 컬럼명 변경 가능: c.title as n_title
//SELECT r.noticegoodno, r.rdate, r.noticeno, c.title as n_title, r.memberno, m.id, m.name
//FROM notice c, noticegood r, member m
//WHERE c.noticeno = r.noticeno AND r.memberno = m.memberno
//ORDER BY noticegoodno DESC;

@Getter @Setter @ToString
public class NoticeNoticegoodMemberVO {
  
  /** 공지사항 추천 번호 */
  private int noticegoodno;
  
  /** 등록일 */
  private String rdate = "";
  
  /** 공지사항 번호 */
  private int noticeno;
  
  /** 공지사항 제목 */
  private String n_title = "";
  
  /** 아이디 */
  private String id = "";
  
  /** 회원 성명 */
  private String name = "";
  
  /** 회원 번호 */
  private int memberno;
}
