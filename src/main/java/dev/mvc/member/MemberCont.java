package dev.mvc.member;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.dto.PageDTO;
import dev.mvc.dto.SearchDTO;
import dev.mvc.grade.GradeProcInter;
import dev.mvc.loginlog.LoginlogProcInter;
import dev.mvc.loginlog.LoginlogVO;
//import dev.mvc.loginlog.LoginlogProcInter;
import dev.mvc.member.MemberVO.UpdateValidationGroup;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/member")
public class MemberCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")  // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.loginlog.LoginlogProc")
  private LoginlogProcInter loginlogProc;
  
  @Autowired
  @Qualifier("dev.mvc.grade.GradeProc")
  private GradeProcInter gradeProc;

  public MemberCont() {
    System.out.println("-> MemberCont created.");
  }
  
  //----------------------------------------------------------------------------------
  // 아이디 및 이메일 중복 확인 메서드 컨트롤러 시작
  // ---------------------------------------------------------------------------------
  /**
   * 아이디 중복 확인
   * @param id
   * @return
   */
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
   * 이메일 중복 확인
   * @param nickname
   * @return
   */
  @GetMapping(value = "/checkEMAIL")
  @ResponseBody
  public String checkEMAIL(@RequestParam(name = "email", defaultValue = "") String email) {
    System.out.println("-> email: " + email);
    int cnt = this.memberProc.checkEMAIL(email);
    System.out.println(cnt);
    
    JSONObject obj = new JSONObject();
    obj.put("cnt", cnt);
    
    return obj.toString();
  }
  //----------------------------------------------------------------------------------
  // 아이디 및 이메일 중복 확인 메서드 컨트롤러 종료
  // ---------------------------------------------------------------------------------
  
  //----------------------------------------------------------------------------------
  // 회원가입 폼 및 처리 메서드 컨트롤러 시작
  // ---------------------------------------------------------------------------------
  /**
   * 회원 가입 폼
   * @param model
   * @param memberVO
   * @return
   */
  @GetMapping(value="/create") // http://localhost:9091/member/create
  public String create_form(Model model, @ModelAttribute("memberVO") MemberVO memberVO) {
    return "/member/create";    // /template/member/create.html
  }
  
  /**
   * 회원 가입 처리
   * @param request
   * @param session
   * @param model
   * @param memberVO
   * @param ra
   * @return
   */
  @PostMapping(value = "/create")
  public String create_proc(HttpServletRequest request,
      HttpSession session,
      Model model, 
      @ModelAttribute("memberVO") MemberVO memberVO,
      RedirectAttributes ra) {

      String pf_img = "";
      String file1saved = ""; 
      String thumb1 = ""; 
      long size1 = 0;
      
      String upDir = Member.getUploadDir();
      
      // 프로필 이미지 파일 처리
      MultipartFile mf = memberVO.getFile1MF();
      if (mf != null && !mf.isEmpty()) {
          pf_img = mf.getOriginalFilename();
          size1 = mf.getSize();
          
          // 파일 검증 및 저장
          if (Tool.checkUploadFile(pf_img)) {
              file1saved = Upload.saveFileSpring(mf, upDir);
              if (Tool.isImage(file1saved)) {
                  thumb1 = Tool.preview(upDir, file1saved, 200, 150); // 썸네일 생성
              }
          } else {
              ra.addFlashAttribute("code", "check_upload_file_fail");
              return "redirect:/member/msg";
          }
      }

      // memberVO에 이미지 정보 설정
      memberVO.setPf_img(pf_img);
      memberVO.setFile1saved(file1saved);
      memberVO.setThumb1(thumb1);
      memberVO.setSize1(size1);

      // ID 및 이메일 중복 체크
      int checkID_cnt = this.memberProc.checkID(memberVO.getId());
      int checkEMAIL_cnt = this.memberProc.checkEMAIL(memberVO.getEmail());

      if (checkID_cnt == 0 && checkEMAIL_cnt == 0) {
          // 회원 등급 설정
          if ("admin".equals(memberVO.getId())) {
              memberVO.setGrade(1); // admin 계정은 GRADE 1로 설정
              memberVO.setGradeno(1); // 관리자 gradeno를 1로 설정
          } else {
              memberVO.setGrade(11); // 기본 회원 11~20
              memberVO.setGradeno(2); // 기본 회원 gradeno 2로 설정
          }
          
          // 기본 이미지 설정 (파일 업로드가 없을 경우)
          if (file1saved.isEmpty()) {
              memberVO.setPf_img("default.png");
              memberVO.setFile1saved("default.png");
              memberVO.setThumb1("default_thumb.png");
              memberVO.setSize1(0);
          }

          // 데이터베이스 처리
          int cnt = this.memberProc.create(memberVO);
          
          // 회원 등록 성공 여부 확인
          if (cnt == 1) {
              model.addAttribute("code", "create_success");
              model.addAttribute("name", memberVO.getName());
              model.addAttribute("id", memberVO.getId());
              model.addAttribute("gradeno", memberVO.getGradeno());
              model.addAttribute("recoveryKey", memberVO.getRecovery_key());
          } else {
              model.addAttribute("code", "create_fail");
          }
          
          model.addAttribute("cnt", cnt);
      } else { // id 중복
          // 중복 ID 또는 이메일이 있는 경우
          if (checkID_cnt > 0) {
              model.addAttribute("code", "duplicate_id"); // 중복 ID 메시지 추가
          }
          if (checkEMAIL_cnt > 0) {
              model.addAttribute("code", "duplicate_email"); // 중복 이메일 메시지 추가
          }
          model.addAttribute("cnt", 0); // 처리된 결과 수 0으로 설정
      }

      return "/member/msg"; // 결과 메시지 페이지로 이동
  }
  //----------------------------------------------------------------------------------
  // 회원가입 폼 및 처리 메서드 컨트롤러 종료
  // ---------------------------------------------------------------------------------
  
  //----------------------------------------------------------------------------------
  // 회원 정보 목록 메서드 컨트롤러 시작 => list_search_paging으로 Update
  // ---------------------------------------------------------------------------------
  
  @GetMapping(value = "/list")
  public String list_search_paging(Model model, HttpSession session,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "searchType", required = false) String searchType,
      @RequestParam(value = "keyword", defaultValue = "") String keyword) {
    if (this.memberProc.isMemberAdmin(session)) {
      
      // 검색 조건
      SearchDTO searchDTO = new SearchDTO();
      searchDTO.setSearchType(searchType);
      searchDTO.setKeyword(keyword);
      searchDTO.setPage(page);
      searchDTO.setSize(page * 10);
      searchDTO.setOffset((page - 1) * 10);
      
      // 전체 회원 수 조회
      int total = this.memberProc.list_search_count(searchDTO);
      
      // 검색 페이지 결과가 없고 페이지가 1보다 큰 경우 첫 페이지로 리다이렉트
      if(total == 0 && page > 1) {
        return "redirect:/member/list?searchType=" + searchType + "&keyword=" + keyword;
      }
      
      // 페이징 정보 계산
      PageDTO pageDTO = new PageDTO(total, page);
      
      // 회원 목록 조회
      ArrayList<MemberVO> list = memberProc.list_search_paging(searchDTO);
      
      model.addAttribute("list", list);
      model.addAttribute("searchDTO", searchDTO);
      model.addAttribute("pageDTO", pageDTO);
      model.addAttribute("total", total);
      
      return "/member/list_search";
    } else {
      return "redirect:/member/login_cookie_need";
    }
  }
  //----------------------------------------------------------------------------------
  // 회원 정보 목록 메서드 컨트롤러 종료
  // ---------------------------------------------------------------------------------

  //----------------------------------------------------------------------------------
  // 회원 정보 조회 메서드 컨트롤러 시작
  // ---------------------------------------------------------------------------------
  @GetMapping(value = "/read")
  public String read(HttpSession session, Model model,
      @RequestParam(name = "memberno", required = false) Integer memberno) {
    
    if (this.memberProc.isMember(session) || this.memberProc.isMemberAdmin(session)) {
      // memberno 파라미터가 없는 경우 세션에서 가져옴 (일반 회원인 경우)
      if (memberno == null) {
        memberno = (int) session.getAttribute("memberno"); // session에서 가져오기
      } else {
        // 관리자가 아닌데 다른 회원의 정보를 조회하려는 경우
        if (!this.memberProc.isMemberAdmin(session)) {
          memberno = (int) session.getAttribute("memberno"); // session에서 가져오기
        }
        // 관리자인 경우 전달받은 memberno 사용
      }

      MemberVO memberVO = this.memberProc.read(memberno);
      model.addAttribute("memberVO", memberVO);

      return "/member/read"; // /templates/th/member/read
    } else {
      return "redirect:/member/login_cookie_need";
    }
  }
  //----------------------------------------------------------------------------------
  // 회원 정보 조회 메서드 컨트롤러 종료
  // ---------------------------------------------------------------------------------
  
  //----------------------------------------------------------------------------------
  // 회원 정보 수정 메서드 컨트롤러 시작
  // ---------------------------------------------------------------------------------
  /**
   * 회원 정보 수정 폼(회원, 관리자 본인 계정만 수정 가능)
   * @param session
   * @param model
   * @param email
   * @param memberno
   * @return
   */
  @GetMapping(value = "/update")
  public String update_form(HttpSession session, Model model,
      @RequestParam(name = "email", required = false) String email,
      @RequestParam(name = "memberno", required = false) Integer memberno) {
    
    // 회원은 회원 등급만 처리
    memberno = (int) session.getAttribute("memberno");
    
    if (this.memberProc.isMember(session) && memberno == (int) session.getAttribute("memberno")) {
      System.out.println("-> memberno: " + memberno);
      MemberVO memberVO = this.memberProc.read(memberno);
      model.addAttribute("memberVO", memberVO);
      
      return "/member/update";
      
    } else if (this.memberProc.isMemberAdmin(session) && memberno == (int) session.getAttribute("memberno")) {
      System.out.println("-> admin memberno: " + memberno);
      MemberVO memberVO = this.memberProc.read(memberno);
      
      model.addAttribute("memberVO", memberVO);
      
      return "/member/update";
    } else {
      return "redirect:/member/login_cookie_nedd";
    }
  }
  
  /**
   * 회원 정보 수정 처리(회원, 관리자 본인 계정만 수정 가능)
   * @param session
   * @param model
   * @param memberVO
   * @return
   */
  @PostMapping(value = "/update")
  public String update_proc(HttpSession session, Model model,
      @Validated(UpdateValidationGroup.class) @ModelAttribute("memberVO") MemberVO memberVO) {
    String grade = (String)session.getAttribute("grade");
    
    System.out.println("-> update-proc called");
    System.out.println("-> grade: " + grade);
    System.out.println("-> memberno: " + memberVO.getMemberno());
    System.out.println("-> session memberno: " + (int)session.getAttribute("memberno"));
    
    // 회원 본인일 때
    if ((grade.equals("member") && memberVO.getMemberno() == (int)session.getAttribute("memberno")) || grade.equals("admin")) {
      int cnt = this.memberProc.update(memberVO);
      System.out.println("update cnt1: " + cnt);
      
      if (cnt == 1) {
        model.addAttribute("code", "update_success");
        model.addAttribute("name", memberVO.getName());
        model.addAttribute("id", memberVO.getId());
        
      } else {
        model.addAttribute("code", "update_fail");
        System.out.println("update_fail");
      }
      
      model.addAttribute("cnt", cnt);
      System.out.println("update_success_cnt: " + cnt);
      
      return "/member/msg";
    } else {
      return "redirect:/member/login_cookie_need";
    }
  }
  //----------------------------------------------------------------------------------
  // 회원 정보 수정 메서드 컨트롤러 종료
  // ---------------------------------------------------------------------------------
  
  //----------------------------------------------------------------------------------
  // 비밀번호 수정 메서드 컨트롤러 시작
  // ---------------------------------------------------------------------------------
  /**
   * 비밀번호 수정 폼
   * @param session
   * @param model
   * @return
   */
  @GetMapping(value = "/passwd_update")
  public String passwd_update_form(HttpSession session, Model model) {
    
    if (this.memberProc.isMember(session)) {
      int memberno = (int) session.getAttribute("memberno");
      
      MemberVO memberVO = this.memberProc.read(memberno);
      
      model.addAttribute("memberVO", memberVO);
      
      return "/member/passwd_update";
    } else if (this.memberProc.isMemberAdmin(session)) {
      int memberno = (int) session.getAttribute("memberno");
      
      MemberVO memberVO = this.memberProc.read(memberno);
      
      model.addAttribute("memberVO", memberVO);
      
      return "/member/passwd_update";
    } else {
      return "redirect:/member/login_cookie_need";
    }
  }
  
  /**
   * 현재 비밀번호 확인
   * @param session
   * @param json_src
   * @return
   */
  @PostMapping(value = "/passwd_check")
  @ResponseBody
  public String passwd_check(HttpSession session, @RequestBody String json_src) {
    System.out.println("json_src: " + json_src);
    
    JSONObject src = new JSONObject(json_src);
    
    String current_passwd = (String) src.get("current_passwd");
    System.out.println("-> current_passwd: " + current_passwd);
    
    int memberno = (int) session.getAttribute("memberno");
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("memberno", memberno);
    map.put("passwd", current_passwd);
    
    int cnt = this.memberProc.passwd_check(map);
    
    JSONObject json = new JSONObject();
    json.put("cnt", cnt);
    System.out.println("cnt -> : " + cnt);
    System.out.println(json.toString());
    
    return json.toString();
  }
  
  /**
   * 비밀번호 수정 처리
   * @param session
   * @param model
   * @param current_passwd
   * @param passwd
   * @return
   */
  @PostMapping(value = "/passwd_update")
  public String passwd_update_proc(HttpSession session, Model model,
      @RequestParam(value = "current_passwd", defaultValue = "") String current_passwd,
      @RequestParam(value = "passwd", defaultValue = "") String passwd) {
    
    System.out.println("asdf");
    if (this.memberProc.isMember(session)) {
      int memberno = (int) session.getAttribute("memberno");
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("memberno", memberno);
      map.put("passwd", current_passwd);
      
      int cnt = this.memberProc.passwd_check(map);
      
      if (cnt == 0) {
        model.addAttribute("code", "passwd_not_equal");
        model.addAttribute("cnt", 0);
      } else {
        map = new HashMap<String, Object>();
        map.put("memberno", memberno);
        map.put("passwd", passwd);
        
        int passwd_change_cnt = this.memberProc.passwd_update(map);
        System.out.println(passwd_change_cnt);
        
        if (passwd_change_cnt == 1) {
          model.addAttribute("code", "passwd_change_success");
          model.addAttribute("cnt", 1);
        } else {
          model.addAttribute("code", "passwd_change_fail");
          model.addAttribute("cnt", 0);
        }
      }
      
      return "redirect:/member/read";
    } else if (this.memberProc.isMemberAdmin(session)) { // 로그인된 관리자일 경우
      int memberno = (int) session.getAttribute("memberno"); // session에서 가져오기
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
        map.put("passwd", passwd);

        int passwd_change_cnt = this.memberProc.passwd_update(map);

        if (passwd_change_cnt == 1) {
          model.addAttribute("code", "passwd_change_success");
          model.addAttribute("cnt", 1);
        } else {
          model.addAttribute("code", "passwd_change_fail");
          model.addAttribute("cnt", 0);
        }
      }
      
      return "redirect:/member/read"; // 수정 처리 하고 새로고침
    } else {
      return "redirect:/member/login_cookie_need";
    }
  }
  //----------------------------------------------------------------------------------
  // 비밀번호 수정 메서드 컨트롤러 종료
  // ---------------------------------------------------------------------------------
  
  //----------------------------------------------------------------------------------
  // 회원 탈퇴 메서드 컨트롤러 시작
  // ---------------------------------------------------------------------------------
  /**
   * 회원 탈퇴 폼
   * @param session
   * @param model
   * @param memberno
   * @return
   */
  @GetMapping(value = "/unsub_delete")
  public String unsub_delete_form(HttpSession session, Model model,
      @RequestParam(name = "memberno", required = false) Integer memberno) {
    
    if (this.memberProc.isMember(session)) {
      memberno = (int) session.getAttribute("memberno");
      MemberVO memberVO = this.memberProc.read(memberno);
      model.addAttribute("memberVO", memberVO);
      System.out.println("memberasdasd");
      System.out.println(memberVO.getMemberno() + "member");
      return "/member/unsub_delete";
    } else if (this.memberProc.isMemberAdmin(session)) {
      memberno = (int) session.getAttribute("memberno");
      MemberVO memberVO = this.memberProc.read(memberno);
      model.addAttribute("memberVO", memberVO);
      System.out.println("adminasdasd");
      return "/member/unsub_delete";
    } else {
      return "redirect:/member/login_cookie_need";
    }
  }
  
  /**
   * 회원 탈퇴 처리
   * @param session
   * @param model
   * @param memberVO
   * @return
   */
  @PostMapping(value = "/unsub_delete")
  public String unsub_delete_process(HttpSession session, Model model, @ModelAttribute("memberVO") MemberVO memberVO) {
//    System.out.println(session.getAttribute("memberno"));
    // 회원 및 회원 본인일 경우
    System.out.println("-> memberno: " + memberVO.getMemberno());
    
    if (memberVO.getMemberno() == (int) session.getAttribute("memberno")) {
      int cnt = this.memberProc.unsub_delete(memberVO);
      model.addAttribute("cnt", cnt);

      if (cnt == 1) {
        model.addAttribute("code", "unsub_success");
        model.addAttribute("memberno", memberVO.getMemberno());
        model.addAttribute("grade", memberVO.getGrade());
        
        System.out.println("-> memberno: " + memberVO.getMemberno() + ", id: " + memberVO.getId() + "email: " + memberVO.getEmail() + " has unsub_delete");
        
        session.invalidate();
        model.addAttribute("code", "unsub_success");
        return "/member/msg"; // templates/member/msg.html
      } else {
        model.addAttribute("code", "unsub_fail");
        return "/member/msg";
      }
    } else {
      return "redirect:/member/login_cookie_need";
    }
  }
  //----------------------------------------------------------------------------------
  // 회원 탈퇴 메서드 컨트롤러 종료
  // ---------------------------------------------------------------------------------

  //----------------------------------------------------------------------------------
  // 회원 프로필 메서드 컨트롤러 종료
  // ---------------------------------------------------------------------------------
  /**
   * 프로필 폼
   * @param session
   * @param model
   * @return
   */
  @GetMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model) {
      Integer memberno = (Integer) session.getAttribute("memberno");
      if (memberno == null) {
          return "redirect:/member/login";
      }

      MemberVO memberVO = this.memberProc.read(memberno);
      if (memberVO == null) {
          return "redirect:/member/login";
      }
      model.addAttribute("memberVO", memberVO);
      return "/member/update_file"; // mypage.html로 이동
  }

  /**
   * 프로필 처리
   * @param session
   * @param memberVO
   * @param ra
   * @return
   */
  @PostMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model,
      RedirectAttributes ra,
      @ModelAttribute("memberVO") MemberVO memberVO) {
    
    MemberVO memberVO_old = memberProc.read(memberVO.getMemberno());
    
    // -------------------------------------------------------------------
    // 파일 삭제 시작
    // -------------------------------------------------------------------
    String file1saved = memberVO_old.getFile1saved(); // 실제 저장된 파일명
    String thumb1 = memberVO_old.getThumb1(); // 실제 저장된 preview 이미지 파일명
    
    long size1 = 0;
    
    // C:/kd/deploy/team2/grade/storage/
    String upDir = Member.getUploadDir();
    
    // 실제 저장된 파일삭제
    Tool.deleteFile(upDir, file1saved);
    // preview 이미지 삭제
    Tool.deleteFile(upDir, thumb1);
    
    // -------------------------------------------------------------------
    // 파일 삭제 종료
    // -------------------------------------------------------------------
    
    // -------------------------------------------------------------------
    // 파일 전송 시작
    // -------------------------------------------------------------------
    String pf_img = "";
    
    MultipartFile mf = memberVO.getFile1MF();
    
    pf_img = mf.getOriginalFilename();
    size1 = mf.getSize();
    
    if (size1 > 0) {
      file1saved = Upload.saveFileSpring(mf, upDir);
      
      if (Tool.isImage(file1saved)) {
        thumb1 = Tool.preview(upDir, file1saved, 250, 200);
      }
    } else {
      pf_img = "";
      file1saved = "";
      thumb1 = "";
      size1 = 0;
    }
    
    memberVO.setPf_img(pf_img);
    memberVO.setFile1saved(file1saved);
    memberVO.setThumb1(thumb1);
    memberVO.setSize1(size1);
    // -------------------------------------------------------------------
    // 파일 전송 코드 종료
    // -------------------------------------------------------------------
    
    this.memberProc.update_file(memberVO);
    ra.addAttribute("memberno", memberVO.getMemberno());
    
    return "redirect:/member/read";
  }

  //----------------------------------------------------------------------------------
  // 로그인 및 로그아웃 메서드 컨트롤러 시작
  // ---------------------------------------------------------------------------------

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
      RedirectAttributes ra,
      @RequestParam(value="id", defaultValue = "") String id, 
      @RequestParam(value="passwd", defaultValue = "") String passwd,
      @RequestParam(value="id_save", defaultValue = "") String id_save,
      @RequestParam(value="passwd_save", defaultValue = "") String passwd_save) {
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("id", id);
    map.put("passwd", passwd);
    
    int cnt = this.memberProc.login(map);
    System.out.println("-> login_proc cnt: " + cnt);
    
    // 로그인 시도한 IP 주소 가져오기
    String ip = request.getRemoteAddr();
    
    // 로그인 로그를 기록하기 위한 객체 생성
    LoginlogVO loginlogVO = new LoginlogVO();
    loginlogVO.setId(id);
    loginlogVO.setIp(ip);
    loginlogVO.setResult(cnt == 1 ? "T" : "F"); // 로그인 성공 여부
    
    // 로그인 기록 저장
    this.loginlogProc.login_log(loginlogVO);
    
    if (cnt == 1) {
      
      // id를 이용하여 회원 정보 조회
      MemberVO memberVO = this.memberProc.readById(id);
      
      if(memberVO.getGrade() == 99) {
        ra.addFlashAttribute("cnt", 0);
        ra.addFlashAttribute("code", "unsub_delete member");
        return "redirect:/member/login";
      }
      
      session.setAttribute("memberno", memberVO.getMemberno());
      session.setAttribute("id", memberVO.getId());
      session.setAttribute("name", memberVO.getName());
      session.setAttribute("email", memberVO.getEmail());
      session.setAttribute("grade", memberVO.getGrade());
      session.setAttribute("file1saved", memberVO.getFile1saved()); // 프로필 이미지 파일 이름 저장
      
      if (memberVO.getGrade() >= 1 && memberVO.getGrade() <= 10) {
        session.setAttribute("grade", "admin");
      } else if (memberVO.getGrade() >= 11 && memberVO.getGrade() <= 20) {
        session.setAttribute("grade", "member");
      } else if (memberVO.getGrade() >= 41 && memberVO.getGrade() <= 49) {
        session.setAttribute("grade", "stopped");
      }
      
      // cancel
      
      
      // Cookie 관련 코드-----------------------------------------------------
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
      // --------------------------------------------------------------------
      // --------------------------------------------------------------------     
      
      return "redirect:/";
    } else { // 로그인 실패
      model.addAttribute("cnt", cnt);
      model.addAttribute("code", "login_fail");
      System.out.println("-> login_fail");
      return "/member/msg";
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
  // Cookie 사용 로그인 관련 코드 종료
  // ----------------------------------------------------------------------------------

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
  
  //----------------------------------------------------------------------------------
  // 로그인 및 로그아웃 메서드 컨트롤러 종료
  // ---------------------------------------------------------------------------------
}
