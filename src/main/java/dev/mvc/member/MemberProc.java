package dev.mvc.member;

import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.http.HttpSession;
import dev.mvc.board.BoardVO;
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
    String grade = (String)session.getAttribute("grade");
    System.out.println("-> grade: " + grade);
    if (grade != null) {
      if (grade.equals("admin") || grade.equals("member")) {
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
    String grade = (String)session.getAttribute("grade");
    
    if (grade != null) {
      if (grade.equals("admin")) {
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
  public int update_text(MemberVO memberVO) {
    int cnt = this.memberDAO.update_text(memberVO);
    return cnt;
  }
  
  @Override
  public int update_file(MemberVO memberVO) {
    int cnt = this.memberDAO.update_file(memberVO);
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
  
  @Override
  public ArrayList<MemberVO> list_by_memberno_search(HashMap<String, Object> hashMap) {
      ArrayList<MemberVO> list = this.memberDAO.list_by_memberno_search(hashMap);
      return list;
  }

  @Override
  public int list_by_memberno_search_count(HashMap<String, Object> hashMap) {
      int cnt = this.memberDAO.list_by_memberno_search_count(hashMap);
      return cnt;
  }

  @Override
  public ArrayList<MemberVO> list_by_memberno_search_paging(HashMap<String, Object> map) {
      int begin_of_page = ((int)map.get("now_page") - 1) * Member.RECORD_PER_PAGE;

      // 시작 rownum 결정
      int start_num = begin_of_page + 1;

      // 종료 rownum
      int end_num = begin_of_page + Member.RECORD_PER_PAGE;

      map.put("start_num", start_num);
      map.put("end_num", end_num);

      ArrayList<MemberVO> list = this.memberDAO.list_by_memberno_search_paging(map);

      return list;
  }

  @Override
  public String pagingBox(int memberno, int now_page, String word, String list_file, int search_count, int record_per_page,
        int page_per_block) {
      int total_page = (int) (Math.ceil((double) search_count / record_per_page));
      int total_grp = (int) (Math.ceil((double) total_page / page_per_block));
      int now_grp = (int) (Math.ceil((double) now_page / page_per_block));

      int start_page = ((now_grp - 1) * page_per_block) + 1;
      int end_page = (now_grp * page_per_block);

      StringBuffer str = new StringBuffer();

      str.append("<style type='text/css'>");
      // CSS 스타일 설정
      str.append("</style>");
      str.append("<DIV id='paging'>");

      int _now_page = (now_grp - 1) * page_per_block;
      if (now_grp >= 2) {
          str.append("<span class='span_box_1'><A href='" + list_file + "?word=" + word + "&now_page=" + _now_page
              + "'>이전</A></span>");
      }

      for (int i = start_page; i <= end_page; i++) {
          if (i > total_page) {
              break;
          }

          if (now_page == i) {
              str.append("<span class='span_box_2'>" + i + "</span>"); // 현재 페이지 강조
          } else {
              str.append("<span class='span_box_1'><A href='" + list_file + "?word=" + word + "&now_page=" + i + "'>" + i
                  + "</A></span>");
          }
      }

      _now_page = (now_grp * page_per_block) + 1;
      if (now_grp < total_grp) {
          str.append("<span class='span_box_1'><A href='" + list_file + "?word=" + word + "&now_page=" + _now_page
              + "'>다음</A></span>");
      }
      str.append("</DIV>");

      return str.toString();
  }


  
}
