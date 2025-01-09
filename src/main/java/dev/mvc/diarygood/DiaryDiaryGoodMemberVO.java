package dev.mvc.diarygood;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class DiaryDiaryGoodMemberVO {
  /**
   *SELECT g.goodno, g.rdate, g.diaryno, d.title, g.memberno, m.id, m.name
FROM diary d, diarygood g, member m
WHERE d.diaryno = g.diaryno AND g.memberno = m.memberno
ORDER BY goodno DESC;
   */
  
  /** 추천 번호 */
  public int goodno;
  
  /** 컨텐츠 번호 */
  public int diaryno;
  
  /** 관리자 권한의 회원 번호 */
  public int memberno;
  
  /** 등록 날짜 */
  public String rdate;
  
  /** 일기 제목 */
  @NotEmpty(message="제목은 필수 사항입니다.")
  @Size(min=1, max=20)
  private String title="";
  
  /** 일기 날짜  */
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date ddate;
  
  /** 날씨 테이블과 연결 */
  private int weatherno;
  
  /** 감정번호 */
  private int emono;
  
  /** 요약일기  */
  private String summary;
  

  private int cnt;
  
  @NotEmpty(message="아이디 입력은 필수입니다.")
  private String id = "";
  
  /** 이메일 주소 */
  @NotEmpty(message="이메일 입력은 필수입니다.")
  private String email="";
  
  /** 가입일 */
  private String mdate = "";
  
  /** 회원 성명 */
  @NotEmpty(message="이름 입력은 필수입니다.")
  private String name = "";
  
  /** 회원 별명 */
  @NotEmpty(message="닉네임 입력은 필수입니다.")
  @Size(min=2, max=12, message="닉네은 최소 2자에서 최대 12자입니다.")
  private String nickname = "";
  
  /** 생년월일 */
  @NotEmpty(message="생년월일 입력은 필수입니다.")
  private String birth = "";

}
