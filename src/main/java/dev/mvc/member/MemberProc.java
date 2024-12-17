package dev.mvc.member;

import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.http.HttpSession;
import dev.mvc.tool.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("dev.mvc.member.MemberProc") // Spring의 컴포넌트로 등록, DI(Dependency Injection)를 통해 사용
public class MemberProc implements MemberProcInter{
  
  @Autowired // MemberDAOInter 타입의 빈을 자동 주입
  private MemberDAOInter memberDAO;
  
  @Autowired // Security 타입의 빈을 자동 주입
  Security security;
  
  @Override
  public int checkID(String id) {
    int cnt = this.memberDAO.checkID(id); // ID 중복 체크
    return cnt;
  }
  
  @Override
  public int create(MemberVO memberVO) {
    // 패스워드 암호화    
    String passwd = memberVO.getPasswd();
    String passwd_encoded = this.security.aesEncode(passwd); // 패스워드 AES 암호화
    memberVO.setPasswd(passwd_encoded);
    
    int cnt = this.memberDAO.create(memberVO); // 회원 생성
    return cnt;
  }
 
  @Override
  public ArrayList<MemberVO> list() {
    ArrayList<MemberVO> list = this.memberDAO.list(); // 회원 목록 조회
    return list;
  }
  
  @Override
  public MemberVO read(int memberno) {
    MemberVO memberVO = this.memberDAO.read(memberno); // 특정 회원 정보 조회
    return memberVO;
  }

  @Override
  public MemberVO readById(String id) {
    MemberVO memberVO = this.memberDAO.readById(id); // ID로 회원 정보 조회
    return memberVO;
  }

  /**
   * 회원인지 검사
   */
  @Override
  public boolean isMember(HttpSession session){
    boolean sw = false; // 로그인하지 않은 것으로 초기화
    String role = (String)session.getAttribute("role");
    
    if (role != null) {
      if (role.equals("admin") || role.equals("member")) {
        sw = true;  // 로그인 한 경우
      }      
    }
    
    return sw;
  }
  
  /**
   * 관리자, 회원인지 검사
   */  
  @Override
  public boolean isMemberAdmin(HttpSession session){
    boolean sw = false; // 로그인하지 않은 것으로 초기화
    String role = (String)session.getAttribute("role");
    
    if (role != null) {
      if (role.equals("admin")) {
        sw = true;  // 관리자 로그인 한 경우
      }      
    }
    
    return sw;
  }
  
  @Override
  public int update(MemberVO memberVO) {
    int cnt = this.memberDAO.update(memberVO); // 회원 정보 수정
    return cnt;
  }
  
  @Override
  public int delete(int memberno) {
    int cnt = this.memberDAO.delete(memberno); // 회원 삭제
    return cnt;
  }
  
  @Override
  public int passwd_check(HashMap<String, Object> map) {
    String passwd = (String)map.get("passwd");
    passwd = this.security.aesEncode(passwd); // 패스워드 AES 암호화
    map.put("passwd", passwd);
    
    int cnt = this.memberDAO.passwd_check(map); // 패스워드 확인
    return cnt;
  }

  @Override
  public int passwd_update(HashMap<String, Object> map) {
    String passwd = (String)map.get("passwd");
    passwd = this.security.aesEncode(passwd); // 패스워드 AES 암호화
    map.put("passwd", passwd);
    
    int cnt = this.memberDAO.passwd_update(map); // 패스워드 업데이트
    return cnt;
  }
  
  @Override
  public int login(HashMap<String, Object> map) {
    String passwd = (String)map.get("passwd");
    passwd = this.security.aesEncode(passwd); // 패스워드 AES 암호화
    map.put("passwd", passwd);
    
    int cnt = this.memberDAO.login(map); // 로그인 처리
    
    return cnt;
  }
  
}
