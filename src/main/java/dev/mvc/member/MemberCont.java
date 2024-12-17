package dev.mvc.member;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dev.mvc.diary.DiaryProcInter;
import dev.mvc.member.MemberVO.Role;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/member")
@Controller
public class MemberCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")  // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.diary.DiaryProc")
  private DiaryProcInter diaryProc;
  
  public MemberCont() {
    System.out.println("-> MemberCont created.");  
  }
  
  @GetMapping(value="/checkID") // http://localhost:9091/member/checkID?id=admin
  @ResponseBody
  public String checkID(@RequestParam(name="id", defaultValue = "") String id) {    
    System.out.println("-> id: " + id);
    int cnt = this.memberProc.checkID(id);
   
    JSONObject obj = new JSONObject();
    obj.put("cnt", cnt);
    
    return obj.toString();
  }
  
  /**
   * 회원 가입 폼
   * @param model
   * @param memberVO
   * @return
   */
//  @GetMapping(value="/create") // http://localhost:9091/member/create
//  public String create_form(Model model, 
//                                      @ModelAttribute("memberVO") MemberVO memberVO) {
//    ArrayList<DiaryVOMenu> menu = this.diaryProc.menu();
//    model.addAttribute("menu", menu);
//    
//    return "/member/create";    // /template/member/create.html
//  }
  
  @PostMapping(value="/create")
  public String create_proc(Model model,
                                     @ModelAttribute("memberVO") MemberVO memberVO) {
    int checkID_cnt = this.memberProc.checkID(memberVO.getId());
    
    if (checkID_cnt == 0) {
      // Role 열거형을 직접 지정하여 기본 회원 설정
      memberVO.setRole(MemberVO.Role.MEMBER);
   
      int cnt = this.memberProc.create(memberVO);
      
      if (cnt == 1) {
        model.addAttribute("code", "create_success");
        model.addAttribute("mname", memberVO.getName());
        model.addAttribute("id", memberVO.getId());
      } else {
        model.addAttribute("code", "create_fail");
      }
      
      model.addAttribute("cnt", cnt);
    } else { // id 중복
      model.addAttribute("code", "duplicte_fail");
      model.addAttribute("cnt", 0);
    }
    
    return "/member/msg"; // /templates/member/msg.html
  }
  
  @GetMapping(value="/list")
  public String list(HttpSession session, Model model) {
//    ArrayList<DiaryVOMenu> menu = this.diaryProc.menu();
//    model.addAttribute("menu", menu);
    
    if (this.memberProc.isMemberAdmin(session)) {
      ArrayList<MemberVO> list = this.memberProc.list();
      
      model.addAttribute("list", list);
      
      return "/member/list";  // /templates/member/list.html
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
  }

  /**
   * 조회
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value="/read")
  public String read(HttpSession session, 
                            Model model,
                            @RequestParam(name="memberno", defaultValue = "") int memberno) {
    // 회원은 회원 등급만 처리, 관리자: 1 ~ 10, 사용자: 11 ~ 20
    // int gradeno = this.memberProc.read(memberno).getGrade(); // 등급 번호
    String grade = (String)session.getAttribute("grade"); // 등급: admin, member, guest
    
    // 사용자: member && 11 ~ 20
    // if (grade.equals("member") && (gradeno >= 11 && gradeno <= 20) && memberno == (int)session.getAttribute("memberno")) {
    if (grade.equals("member") &&  memberno == (int)session.getAttribute("memberno")) {
      System.out.println("-> read memberno: " + memberno);
      
      MemberVO memberVO = this.memberProc.read(memberno);
      model.addAttribute("memberVO", memberVO);
      
      return "member/read";  // templates/member/read.html
      
    } else if (grade.equals("admin")) {
      System.out.println("-> read memberno: " + memberno);
      
      MemberVO memberVO = this.memberProc.read(memberno);
      model.addAttribute("memberVO", memberVO);
      
      return "member/read";  // templates/member/read.html
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
    
  }
  
  /**
   * 수정 처리
   * @param model
   * @param memberVO
   * @return
   */
  @PostMapping(value="/update")
  public String update_proc(HttpSession session,
                                       Model model, 
                                       @ModelAttribute("memberVO") MemberVO memberVO) {
    String grade = (String)session.getAttribute("grade"); // 등급: admin, member, guest

    // 회원 본인이거나 관리자인경우 처리
    if ((grade.equals("member") &&  memberVO.getMemberno() == (int)session.getAttribute("memberno")) ||  grade.equals("admin")) {
      int cnt = this.memberProc.update(memberVO); // 수정
      
      if (cnt == 1) {
        model.addAttribute("code", "update_success");
        model.addAttribute("mname", memberVO.getName());
        model.addAttribute("id", memberVO.getId());
      } else {
        model.addAttribute("code", "update_fail");
      }
      
      model.addAttribute("cnt", cnt);
      
      return "/member/msg"; // /templates/member/msg.html
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
    
  }
  
  /**
   * 삭제
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value="/delete")
  public String delete(HttpSession session,
                              Model model,
                              @RequestParam(name="memberno", defaultValue = "") int memberno) {
    if (this.memberProc.isMemberAdmin(session)) {
      System.out.println("-> delete memberno: " + memberno);
      
      MemberVO memberVO = this.memberProc.read(memberno);
      model.addAttribute("memberVO", memberVO);
      
      return "/member/delete";  // templates/member/delete.html
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
    
  }
  
  /**
   * 회원 Delete process
   * @param model
   * @param memberno 삭제할 레코드 번호
   * @return
   */
  @PostMapping(value="/delete")
  public String delete_process(HttpSession session,
                                          Model model,
                                          @RequestParam(name="memberno", defaultValue = "") int memberno) {
    if (this.memberProc.isMemberAdmin(session)) {
      int cnt = this.memberProc.delete(memberno);
      
      if (cnt == 1) {
        return "redirect:/member/list";
      } else {
        model.addAttribute("code", "delete_fail");
        return "/member/msg"; // /templates/member/msg.html
      }
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
  }
  
  /**
   * 로그아웃
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value="/logout")
  public String logout(HttpSession session, Model model) {
    session.invalidate();  // 모든 세션 변수 삭제
    return "redirect:/";
  }

  // ----------------------------------------------------------------------------------
  // Cookie 사용 로그인 관련 코드 시작
  // ----------------------------------------------------------------------------------
  /**
   * 로그인
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value="/login")
  public String login_form(Model model, HttpServletRequest request) {
    
    // Cookie 관련 코드---------------------------------------------------------
    Cookie[] cookies = request.getCookies();
    Cookie cookie = null;
  
    String ck_id = ""; // id 저장
    String ck_id_save = ""; // id 저장 여부를 체크
    String ck_passwd = ""; // passwd 저장
    String ck_passwd_save = ""; // passwd 저장 여부를 체크
  
    if (cookies != null) { // 쿠키가 존재한다면
      for (int i=0; i < cookies.length; i++){
        cookie = cookies[i]; // 쿠키 객체 추출
      
        if (cookie.getName().equals("ck_id")){                     // 아이디
          ck_id = cookie.getValue();  // email
        }else if(cookie.getName().equals("ck_id_save")){        // 아이디 저장 여부
          ck_id_save = cookie.getValue();  // Y, N
        }else if (cookie.getName().equals("ck_passwd")){       // 패스워드
          ck_passwd = cookie.getValue();         // 1234
        }else if(cookie.getName().equals("ck_passwd_save")){ // 패스워드 저장 여부
          ck_passwd_save = cookie.getValue();  // Y, N
        }
      }
    }
    // ----------------------------------------------------------------------------
    model.addAttribute("ck_id", ck_id);
    model.addAttribute("ck_id_save", ck_id_save);
    model.addAttribute("ck_passwd", ck_passwd);
    model.addAttribute("ck_passwd_save", ck_passwd_save);
    
    return "/member/login_cookie";  // /templates/member/login_cookie.html
  }

  /**
   * Cookie 기반 로그인 처리
   * @param session
   * @param request
   * @param response
   * @param model
   * @param id 아이디
   * @param passwd 패스워드
   * @param id_save 아이디 저장 여부
   * @param passwd_save 패스워드 저장 여부
   * @return
   */
  @PostMapping(value="/login")
  public String login_proc(HttpSession session,
                                     HttpServletRequest request,
                                     HttpServletResponse response,
                                     Model model, 
                                     @RequestParam(value="id", defaultValue = "") String id, 
                                     @RequestParam(value="passwd", defaultValue = "") String passwd,
                                     @RequestParam(value="id_save", defaultValue = "") String id_save,
                                     @RequestParam(value="passwd_save", defaultValue = "") String passwd_save
                                     ) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("id", id);
    map.put("passwd", passwd);
    
    int cnt = this.memberProc.login(map);
    System.out.println("-> login_proc cnt: " + cnt);
    
    model.addAttribute("cnt", cnt);
    
    if (cnt == 1) {
      // id를 이용하여 회원 정보 조회
      MemberVO memverVO = this.memberProc.readById(id);
      session.setAttribute("memberno", memverVO.getMemberno());
      session.setAttribute("id", memverVO.getId());
      session.setAttribute("name", memverVO.getName());
      
      if (memverVO.getRole().ordinal() >= Role.ADMIN.ordinal() && memverVO.getRole().ordinal() <= Role.ADMIN.ordinal() + 9) {
        session.setAttribute("role", "admin");
      } else if (memverVO.getRole().ordinal() >= Role.MEMBER.ordinal() && memverVO.getRole().ordinal() <= Role.MEMBER.ordinal() + 9) {
          session.setAttribute("role", "member");
      } else if (memverVO.getRole().ordinal() >= Role.GUEST.ordinal()) { // GUEST가 마지막 역할이라고 가정
          session.setAttribute("role", "guest");
      }

      
      // Cookie 관련 코드---------------------------------------------------------
      // -------------------------------------------------------------------
      // id 관련 쿠기 저장
      // -------------------------------------------------------------------
      if (id_save.equals("Y")) { // id를 저장할 경우, Checkbox를 체크한 경우
        Cookie ck_id = new Cookie("ck_id", id);
        ck_id.setPath("/");  // root 폴더에 쿠키를 기록함으로 모든 경로에서 쿠기 접근 가능
        ck_id.setMaxAge(60 * 60 * 24 * 30); // 30 day, 초단위
        response.addCookie(ck_id); // id 저장
      } else { // N, id를 저장하지 않는 경우, Checkbox를 체크 해제한 경우
        Cookie ck_id = new Cookie("ck_id", "");
        ck_id.setPath("/");
        ck_id.setMaxAge(0); // 0초
        response.addCookie(ck_id); // id 저장
      }
      
      // id를 저장할지 선택하는  CheckBox 체크 여부
      Cookie ck_id_save = new Cookie("ck_id_save", id_save);
      ck_id_save.setPath("/");
      ck_id_save.setMaxAge(60 * 60 * 24 * 30); // 30 day
      response.addCookie(ck_id_save);
      // -------------------------------------------------------------------
  
      // -------------------------------------------------------------------
      // Password 관련 쿠기 저장
      // -------------------------------------------------------------------
      if (passwd_save.equals("Y")) { // 패스워드 저장할 경우
        Cookie ck_passwd = new Cookie("ck_passwd", passwd);
        ck_passwd.setPath("/");
        ck_passwd.setMaxAge(60 * 60 * 24 * 30); // 30 day
        response.addCookie(ck_passwd);
      } else { // N, 패스워드를 저장하지 않을 경우
        Cookie ck_passwd = new Cookie("ck_passwd", "");
        ck_passwd.setPath("/");
        ck_passwd.setMaxAge(0);
        response.addCookie(ck_passwd);
      }
      // passwd를 저장할지 선택하는  CheckBox 체크 여부
      Cookie ck_passwd_save = new Cookie("ck_passwd_save", passwd_save);
      ck_passwd_save.setPath("/");
      ck_passwd_save.setMaxAge(60 * 60 * 24 * 30); // 30 day
      response.addCookie(ck_passwd_save);
      // -------------------------------------------------------------------
      // ----------------------------------------------------------------------------     
      
      return "redirect:/";
    } else {
      model.addAttribute("code", "login_fail");
      return "/member/msg";
    }
  }
  
  // ----------------------------------------------------------------------------------
  // Cookie 사용 로그인 관련 코드 종료
  // ----------------------------------------------------------------------------------
  
  /**
   * 패스워드 수정 폼
   * @param model
   * @param memberno
   * @return
   */
  @GetMapping(value="/passwd_update")
  public String passwd_update_form(HttpSession session, Model model) {
//    ArrayList<DiaryVOMenu> menu = this.diaryProc.menu();
//    model.addAttribute("menu", menu);
    
    if (this.memberProc.isMember(session)) {
      int memberno = (int)session.getAttribute("memberno"); // session에서 가져오기
      
      MemberVO memberVO = this.memberProc.read(memberno);
      
      model.addAttribute("memberVO", memberVO);
      
      return "/member/passwd_update";    // /templates/member/passwd_update.html      
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }

  }
  
  /**
   * 현재 패스워드 확인
   * @param session
   * @param current_passwd
   * @return 1: 일치, 0: 불일치
   */
  @PostMapping(value="/passwd_check")
  @ResponseBody
  public String passwd_check(HttpSession session, @RequestBody String json_src) {
    System.out.println("-> json_src: " + json_src); // json_src: {"current_passwd":"1234"}
    
    JSONObject src = new JSONObject(json_src); // String -> JSON
    
    String current_passwd = (String)src.get("current_passwd"); // 값 가져오기
    System.out.println("-> current_passwd: " + current_passwd);
    
    try {
      Thread.sleep(3000);
    } catch(Exception e) {
      
    }
    
    int memberno = (int)session.getAttribute("memberno"); // session에서 가져오기
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("memberno", memberno);
    map.put("passwd", current_passwd);
    
    int cnt = this.memberProc.passwd_check(map); // 현재 로그인한 사용자의 패스워드 확인
    
    JSONObject json = new JSONObject();
    json.put("cnt", cnt);  // 1: 패스워드 일치, 0: 불일치
    System.out.println(json.toString());
    
    return json.toString();   
  }
  
  /**
   * 패스워드 변경
   * @param session
   * @param model
   * @param current_passwd 현재 패스워드
   * @param passwd 새로운 패스워드
   * @return
   */
  @PostMapping(value="/passwd_update_proc")
  public String passwd_update_proc(HttpSession session, 
                                                    Model model, 
                                                    @RequestParam(value="current_passwd", defaultValue = "") String current_passwd, 
                                                    @RequestParam(value="passwd", defaultValue = "") String passwd) {
    
    if (this.memberProc.isMember(session)) {
      int memberno = (int)session.getAttribute("memberno"); // session에서 가져오기
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("memberno", memberno);
      map.put("passwd", current_passwd);
   
      int cnt = this.memberProc.passwd_check(map);
      
      if (cnt == 0) { // 패스워드 불일치
        model.addAttribute("code", "passwd_not_equal");
        model.addAttribute("cnt", 0);
        
      } else { // 패스워드 일치
        map = new HashMap<String, Object>();
        map.put("memberno", memberno);
        map.put("passwd", passwd); // 새로운 패스워드

        int passwd_change_cnt = this.memberProc.passwd_update(map);
        
        if (passwd_change_cnt == 1) {
          model.addAttribute("code", "passwd_change_success");
          model.addAttribute("cnt", 1);
        } else {
          model.addAttribute("code", "passwd_change_fail");
          model.addAttribute("cnt", 0);
        }
      }

      return "/member/msg";   // /templates/member/msg.html
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }

  }

  /**
   * 로그인 요구에 따른 로그인 폼 출력 
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value="/login_cookie_need")
  public String login_cookie_need(Model model, HttpServletRequest request) {
    // Cookie 관련 코드---------------------------------------------------------
    Cookie[] cookies = request.getCookies();
    Cookie cookie = null;
  
    String ck_id = ""; // id 저장
    String ck_id_save = ""; // id 저장 여부를 체크
    String ck_passwd = ""; // passwd 저장
    String ck_passwd_save = ""; // passwd 저장 여부를 체크
  
    if (cookies != null) { // 쿠키가 존재한다면
      for (int i=0; i < cookies.length; i++){
        cookie = cookies[i]; // 쿠키 객체 추출
      
        if (cookie.getName().equals("ck_id")){
          ck_id = cookie.getValue();  // email
        }else if(cookie.getName().equals("ck_id_save")){
          ck_id_save = cookie.getValue();  // Y, N
        }else if (cookie.getName().equals("ck_passwd")){
          ck_passwd = cookie.getValue();         // 1234
        }else if(cookie.getName().equals("ck_passwd_save")){
          ck_passwd_save = cookie.getValue();  // Y, N
        }
      }
    }
    // ----------------------------------------------------------------------------
    model.addAttribute("ck_id", ck_id);
    model.addAttribute("ck_id_save", ck_id_save);
    model.addAttribute("ck_passwd", ck_passwd);
    model.addAttribute("ck_passwd_save", ck_passwd_save);
    return "/member/login_cookie_need";  // templates/member/login_cookie_need.html
  }
}