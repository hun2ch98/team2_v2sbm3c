package dev.mvc.member;

import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.http.HttpSession;
import dev.mvc.tool.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.member.MemberProc") // Spring의 컴포넌트로 등록, DI(Dependency Injection)를 통해 사용
public class MemberProc implements MemberProcInter {
  
  @Autowired // MemberDAOInter 타입의 빈을 자동 주입
  private MemberDAOInter memberDAO;
  
  @Autowired // Security 타입의 빈을 자동 주입
  Security security;

  //----------------------------------------------------------------------------------
  // ~~~~~~ 회원 관련 메서드 시작
  // ---------------------------------------------------------------------------------
  
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
  public ArrayList<MemberVO> list_all() {
    ArrayList<MemberVO> list = this.memberDAO.list_all(); // 회원 목록 조회
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

  //----------------------------------------------------------------------------------
  // ~~~~~~ 회원 관련 메서드 종료
  // ---------------------------------------------------------------------------------


  //----------------------------------------------------------------------------------
  // ~~~~~~ 로그인 및 인증 관련 메서드 시작
  // ---------------------------------------------------------------------------------
  
  @Override
  public boolean isMember(HttpSession session) {
    boolean sw = false; // 로그인하지 않은 것으로 초기화
    String grade = (String)session.getAttribute("grade");
    if (grade != null) {
      if (grade.equals("admin") || grade.equals("member")) {
        sw = true;  // 로그인 한 경우
      }      
    }
    return sw;
  }
  
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

  @Override
  public int login(HashMap<String, Object> map) {
    String passwd = (String)map.get("passwd");
    passwd = this.security.aesEncode(passwd); // 패스워드 AES 암호화
    map.put("passwd", passwd);
    
    int cnt = this.memberDAO.login(map); // 로그인 처리
    return cnt;
  }

  //----------------------------------------------------------------------------------
  // ~~~~~~ 로그인 및 인증 관련 메서드 종료
  // ---------------------------------------------------------------------------------


  //----------------------------------------------------------------------------------
  // ~~~~~~ 패스워드 관련 메서드 시작
  // ---------------------------------------------------------------------------------

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

  //----------------------------------------------------------------------------------
  // ~~~~~~ 패스워드 관련 메서드 종료
  // ---------------------------------------------------------------------------------


  //----------------------------------------------------------------------------------
  // ~~~~~~ 목록 조회 및 페이지네이션 관련 메서드 시작
  // ---------------------------------------------------------------------------------
  
  @Override
  public ArrayList<MemberVO> list_by_memberno_search(HashMap<String, Object> hashMap) {
    ArrayList<MemberVO> list = this.memberDAO.list_by_memberno_search_paging(hashMap);
    return list;
  }
  
  @Override
  public int list_by_memberno_search_count(HashMap<String, Object> map) {
    int cnt = this.memberDAO.list_by_memberno_search_count(map); // 검색된 회원 수 조회
    return cnt;
  }

  @Override
  public ArrayList<MemberVO> list_by_memberno_search_paging(HashMap<String, Object> map) {
    int now_page = (int) map.get("now_page");
    int record_per_page = 10; // 페이지당 레코드 수
    
    int startRow = (now_page - 1) * record_per_page + 1;
    int endRow = now_page * record_per_page;

    // 계산된 값을 `HashMap`에 추가합니다.
    map.put("startRow", startRow);
    map.put("endRow", endRow);
    
    // 데이터베이스 쿼리 실행
    ArrayList<MemberVO> list = this.memberDAO.list_by_memberno_search_paging(map);
    return list;
  }

  @Override
  public String pagingBox(int now_page, String id, String list_file, int search_count, int record_per_page,
        int page_per_block) {
    
      // 전체 페이지 수 계산
      int total_page = (int) Math.ceil((double) search_count / record_per_page);
      int total_grp = (int) Math.ceil((double) total_page / page_per_block);
      int now_grp = (int) Math.ceil((double) now_page / page_per_block);
    
      // 현재 그룹의 시작 페이지와 끝 페이지
      int start_page = ((now_grp - 1) * page_per_block) + 1;
      int end_page = now_grp * page_per_block;
    
      StringBuffer str = new StringBuffer(); // 문자열 생성
      
      // 스타일 설정
      str.append("<style type='text/css'>");
      str.append("  #paging {text-align: center; margin-top: 5px; font-size: 1em;}");
      str.append("  #paging A:link {text-decoration:none; color:black; font-size: 1em;}");
      str.append("  #paging A:hover{text-decoration:none; background-color: #FFFFFF; color:black; font-size: 1em;}");
      str.append("  #paging A:visited {text-decoration:none;color:black; font-size: 1em;}");
      str.append("  .span_box_1{");
      str.append("    text-align: center;");
      str.append("    font-size: 1em;");
      str.append("    border: 1px;");
      str.append("    border-style: solid;");
      str.append("    border-color: #cccccc;");
      str.append("    padding:1px 6px 1px 6px; /*위, 오른쪽, 아래, 왼쪽*/");
      str.append("    margin:1px 2px 1px 2px; /*위, 오른쪽, 아래, 왼쪽*/");
      str.append("  }");
      str.append("  .span_box_2{");
      str.append("    text-align: center;");
      str.append("    background-color: #668db4;");
      str.append("    color: #FFFFFF;");
      str.append("    font-size: 1em;");
      str.append("    border: 1px;");
      str.append("    border-style: solid;");
      str.append("    border-color: #cccccc;");
      str.append("    padding:1px 6px 1px 6px; /*위, 오른쪽, 아래, 왼쪽*/");
      str.append("    margin:1px 2px 1px 2px; /*위, 오른쪽, 아래, 왼쪽*/");
      str.append("  }");
      str.append("</style>");
      str.append("<DIV id='paging'>");
      
      // 이전 그룹 링크
      int _now_page = (now_grp - 1) * page_per_block;
      if (now_grp >= 2) {
        str.append("<span class='span_box_1'><A href='" + list_file + "?id=" + id + "&now_page=" + _now_page + "'>이전</A></span>");
      }
      
      // 중앙의 페이지 목록
      for (int i = start_page; i <= end_page; i++) {
        if (i > total_page) {
          break;
        }
        
        if (now_page == i) {
          str.append("<span class='span_box_2'>" + i + "</span>"); // 현재 페이지 강조
        } else {
          str.append("<span class='span_box_1'><A href='" + list_file + "?id=" + id + "&now_page=" + i + "'>" + i + "</A></span>");
        }
      }
      
      // 다음 그룹 링크
      _now_page = (now_grp * page_per_block) + 1;
      if (now_grp < total_grp) {
        str.append("<span class='span_box_1'><A href='" + list_file + "?id=" + id + "&now_page=" + _now_page + "'>다음</A></span>");
      }
      str.append("</DIV>");

      return str.toString();
  }

  //----------------------------------------------------------------------------------
  // ~~~~~~ 목록 조회 및 페이지네이션 관련 메서드 종료
  // ---------------------------------------------------------------------------------


  //----------------------------------------------------------------------------------
  // ~~~~~~ 기타 메서드 시작
  // ---------------------------------------------------------------------------------

  @Override
  public int update_text(MemberVO memberVO) {
    int cnt = this.memberDAO.update_text(memberVO); // 회원 텍스트 정보 수정
    return cnt;
  }
  
  @Override
  public int update_file(MemberVO memberVO) {
    int cnt = this.memberDAO.update_file(memberVO); // 회원 파일 정보 수정
    return cnt;
  }
  
  //----------------------------------------------------------------------------------
  // ~~~~~~ 기타 메서드 종료
  // ---------------------------------------------------------------------------------
}
