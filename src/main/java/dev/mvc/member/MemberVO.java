package dev.mvc.member;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MemberVO {
  /*
  memberno NUMBER(100)       NOT NULL, -- 회원 번호, 레코드를 구분하는 컬럼 
  id                VARCHAR(10)     NOT NULL UNIQUE, -- 아이디, 중복 안됨, 레코드를 구분
  passwd       VARCHAR(255)   NOT NULL, -- 패스워드, 영숫자 조합, 암호화
  email          VARCHAR(50)     NOT NULL UNIQUE,  -- 이메일, 중복 안됨, 비밀번호 찾기용(인증코드 전송)
  name       VARCHAR(30)     NOT NULL, -- 성명, 한글 10자 저장 가능
  nickname    VARCHAR(10)     NULL, -- 별명 
  birth          VARCHAR(20)            NOT NULL, -- 생년월일
  address       VARCHAR(100)     NULL, -- 주소 1 
  role             NUMBER(1)            NOT NULL, -- 역할
  pf_img  VARCHAR(255) NULL -- 프로필 이미지
  */
  
  public enum Role {
    ADMIN(1), 
    MEMBER(0), // 회원 역할의 기본 값
    GUEST(2); // GUEST 역할의 기본 값
    private final int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
} 

    /** 회원 번호 */
    private int memberno;
    /** 아이디(이메일) */
    private String id = "";
    /** 패스워드 */
    private String passwd = "";
    /** 이메일 주소 */
    private String email="";
    /** 회원 성명 */
    private String name = "";
    /** 회원 별명 */
    private String nickname = "";
    /** 생년월일 */
    private String birth = "";
    /** 주소 */
    private String address = "";
    /** 역할 설정 : 0(회원), 1(관리자), 2(GUEST)만 허용 */
    private Role role = Role.MEMBER; //기본값   
    /** 프로필 이미지 */
    private String pf_img = "";
    /** 등록된 패스워드 */
    private String old_passwd = "";
    /** id 저장 여부 */
    private String id_save = "";
    /** passwd 저장 여부 */
    private String passwd_save = "";
    /** 이동할 주소 저장 */
    private String url_address = "";
    
    
}

