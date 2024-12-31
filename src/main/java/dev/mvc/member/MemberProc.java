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
  public ArrayList<MemberVO> list_by_memberno_search_paging(HashMap<String, Object> map) {
    // `now_page`를 기반으로 `startRow`와 `endRow`를 계산합니다.
    int now_page = (int) map.get("now_page");
    int record_per_page = 10; // 페이지당 레코드 수
    
    int startRow = (now_page - 1) * record_per_page + 1;
    int endRow = now_page * record_per_page;

    // 계산된 값을 `HashMap`에 추가합니다.
    map.put("startRow", startRow);
    map.put("endRow", endRow);

    ArrayList<MemberVO> list = this.memberDAO.list_by_memberno_search_paging(map);

    return list;
  }

  @Override
  public String pagingBox(int now_page, String name, String list_file, int search_count, int record_per_page,
        int page_per_block) {
    
      // 전체 페이지 수 계산
      int total_page = (int) Math.ceil((double) search_count / record_per_page);
      // 전체 그룹 수 계산
      int total_grp = (int) Math.ceil((double) total_page / page_per_block);
      // 현재 그룹 계산
      int now_grp = (int) Math.ceil((double) now_page / page_per_block);
    
      // 현재 그룹의 시작 페이지와 끝 페이지
      int start_page = ((now_grp - 1) * page_per_block) + 1;
      int end_page = now_grp * page_per_block;
    
      StringBuffer str = new StringBuffer(); // String class 보다 문자열 추가등의 편집시 속도가 빠름

      // style이 java 파일에 명시되는 경우는 로직에 따라 css가 영향을 많이 받는 경우에 사용하는 방법
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
      str.append("<div id='paging'>");
      
      // 이전 그룹 링크
      int _now_page = (now_grp - 1) * page_per_block;
      if (now_grp >= 2) { // 현재 그룹번호가 2이상이면 페이지수가 11페이지 이상임으로 이전 그룹으로 갈수 있는 링크 생성

        str.append("<span class='span_box_1'><A href='" + list_file + "?name=" + name + "&now_page=" + _now_page
            + "'>이전</A></span>");
      }
      
      // 중앙의 페이지 목록
      for (int i = start_page; i <= end_page; i++) {
        if (i > total_page) { // 마지막 페이지를 넘어갔다면 페이 출력 종료
          break;
        }
        
        if (now_page == i) { // 목록에 출력하는 페이지가 현재페이지와 같다면 CSS 강조(차별을 둠)
          str.append("<span class='span_box_2'>" + i + "</span>"); // 현재 페이지, 강조
        } else {
          // 현재 페이지가 아닌 페이지는 이동이 가능하도록 링크를 설정
          str.append("<span class='span_box_1'><A href='" + list_file + "?name=" + name + "&now_page=" + i + "'>" + i
              + "</A></span>");
        }
      }
      
      // 10개 다음 페이지로 이동
      // nowGrp: 1 (1 ~ 10 page), nowGrp: 2 (11 ~ 20 page), nowGrp: 3 (21 ~ 30 page)
      // 현재 페이지 5일경우 -> 현재 1그룹: (1 * 10) + 1 = 2그룹의 시작페이지 11
      // 현재 페이지 15일경우 -> 현재 2그룹: (2 * 10) + 1 = 3그룹의 시작페이지 21
      // 현재 페이지 25일경우 -> 현재 3그룹: (3 * 10) + 1 = 4그룹의 시작페이지 31
      _now_page = (now_grp * page_per_block) + 1; // 최대 페이지수 + 1
      if (now_grp < total_grp) {
        str.append("<span class='span_box_1'><A href='" + list_file + "?name=" + name + "&now_page=" + _now_page
            + "'>다음</A></span>");
      }
      str.append("</div>");

      return str.toString();
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
  public String getNickname(int memberno) {
      return memberDAO.getNickname(memberno); // DAO에서 nickname 가져옴
  }
}
