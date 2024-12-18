package dev.mvc.member;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MemberVO {
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
    /** 등급 */
    private int role = 0;
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

