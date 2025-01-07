package dev.mvc.member;

import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.http.HttpSession;
import dev.mvc.dto.SearchDTO;
import dev.mvc.tool.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.member.MemberProc") // Spring의 컴포넌트로 등록, DI(Dependency Injection)를 통해 사용
public class MemberProc implements MemberProcInter {
  
  @Autowired // MemberDAOInter 타입의 빈을 자동 주입
  private MemberDAOInter memberDAO;
  
  @Autowired // Security 타입의 빈을 자동 주입
  Security security;
  
  public MemberProc(){
    System.out.println("-> MemberProc 생성됨.");
  }

  //----------------------------------------------------------------------------------
  // ~~~~~~ 회원 관련 메서드 시작
  // ---------------------------------------------------------------------------------
  
  /** 아이디 중복 확인 */
  @Override
  public int checkID(String id) {
    int cnt = this.memberDAO.checkID(id); // ID 중복 체크
    return cnt;
  }
  
  /**
   * 이메일 중복 검사
   */
  @Override
  public int checkEMAIL(String email) {
    int cnt = this.memberDAO.checkEMAIL(email);
    return cnt;
  }
  
  /** 비밀번호 암호화, 회원가입 처리 */
  @Override
  public int create(MemberVO memberVO) {
    // 패스워드 암호화    
    String passwd = memberVO.getPasswd();
    String passwd_encoded = this.security.aesEncode(passwd); // 패스워드 AES 암호화
    memberVO.setPasswd(passwd_encoded);
    
    int cnt = this.memberDAO.create(memberVO); // 회원 생성
    return cnt;
  }
  
  /** 조건에 맞는 회원 수 */
  @Override
  public int list_search_count(SearchDTO searchDTO) {
    return memberDAO.list_search_count(searchDTO);
  }
  
  /** 회원 검색 + 목록 페이징 */
  @Override
  public ArrayList<MemberVO> list_search_paging(SearchDTO searchDTO) {
    return memberDAO.list_search_paging(searchDTO);
  }
  
  /** 회원 번호(memberno)로 회원 정보 조회 */
  @Override
  public MemberVO read(int memberno) {
    MemberVO memberVO = this.memberDAO.read(memberno); // 특정 회원 정보 조회
    return memberVO;
  }
  
  /** id로 회원 정보 조회 */
  @Override
  public MemberVO readById(String id) {
    MemberVO memberVO = this.memberDAO.readById(id); // ID로 회원 정보 조회
    return memberVO;
  }
  //----------------------------------------------------------------------------------
  // ~~~~~~ 회원 관련 메서드 종료
  // ---------------------------------------------------------------------------------


  //----------------------------------------------------------------------------------
  // ~~~~~~ 로그인 및 패스워드 인증 관련 메서드 시작
  // ---------------------------------------------------------------------------------
  
  /** 회원인지 검사 */
  @Override
  public boolean isMember(HttpSession session) {
    boolean sw = false; // 로그인하지 않은 것으로 초기화
    String grade = (String)session.getAttribute("grade");
    
    if (grade != null) {
      if (grade.equals("member")) {
        sw = true;  // 로그인 한 경우
      }      
    }
    
    return sw;
  }
  
  /** 관리자인지 검사 */
  @Override
  public boolean isMemberAdmin(HttpSession session) {
    boolean sw = false; // 로그인하지 않은 것으로 초기화
    String grade = (String)session.getAttribute("grade");
    
    if (grade != null) {
      if (grade.equals("admin")) {
        sw = true;  // 관리자 로그인 한 경우
      }      
    }
    return sw;
  }
  
  /** 회원 정보 수정 */
  @Override
  public int update(MemberVO memberVO) {
    int cnt = this.memberDAO.update(memberVO); // 회원 정보 수정
    return cnt;
  }
  
  /** 회원 탈퇴 처리 */
  @Override
  public int unsub_delete(MemberVO memberVO) {
    int cnt = this.memberDAO.unsub_delete(memberVO);
    return cnt;
  }
  
  /** 로그인 처리 */
  @Override
  public int login(HashMap<String, Object> map) {
    String passwd = (String)map.get("passwd");
    passwd = this.security.aesEncode(passwd); // 패스워드 AES 암호화
    map.put("passwd", passwd);
    
    int cnt = this.memberDAO.login(map); // 로그인 처리
    return cnt;
  }
  
  /** 현재 비밀번호 검사 */
  @Override
  public int passwd_check(HashMap<String, Object> map) {
    String passwd = (String)map.get("passwd");
    passwd = this.security.aesEncode(passwd); // 패스워드 AES 암호화
    map.put("passwd", passwd);
    
    int cnt = this.memberDAO.passwd_check(map); // 패스워드 확인
    return cnt;
  }
  
  /** 비밀번호 변경 처리 */
  @Override
  public int passwd_update(HashMap<String, Object> map) {
    String passwd = (String)map.get("passwd");
    passwd = this.security.aesEncode(passwd); // 패스워드 AES 암호화
    map.put("passwd", passwd);
    
    int cnt = this.memberDAO.passwd_update(map); // 패스워드 업데이트
    return cnt;
  }
  
  @Override
  public int update_passwd_find(HashMap<String, Object> map) {
    String passwd = (String)map.get("passwd");
    passwd = this.security.aesEncode(passwd);
    map.put("passwd", passwd);
    
    int cnt = this.memberDAO.update_passwd_find(map);
    return cnt;
  }
  
  /** 이름, 이메일 일치하는 회원 검사 */
  @Override
  public int find_id_check(HashMap<String, String> map) {
    int cnt = this.memberDAO.find_id_check(map);
    return cnt;
  }
  
  /** 아이디, 이메일 일치하는 회원 검사 */
  @Override
  public int find_passwd(String id, String phone) {
    int cnt = this.memberDAO.find_passwd(id, phone);
    
    return cnt;
  }

  //----------------------------------------------------------------------------------
  // ~~~~~~ 로그인 및 패스워드 인증 관련 메서드 종료
  // ---------------------------------------------------------------------------------

  
  //----------------------------------------------------------------------------------
  // ~~~~~~ Mypage 프로필 이미지 수정 메서드 시작
  // ---------------------------------------------------------------------------------

  @Override
  public int update_file(MemberVO memberVO) {
    int cnt = this.memberDAO.update_file(memberVO); // 회원 파일 정보 수정
    return cnt;
  }
  
  //----------------------------------------------------------------------------------
  // ~~~~~~ Mypage 프로필 이미지 수정 메서드 종료
  // --------------------------------------------------------------------------------- 
}
