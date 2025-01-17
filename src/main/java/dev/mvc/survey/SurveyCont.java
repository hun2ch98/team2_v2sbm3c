package dev.mvc.survey;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.log.LogProcInter;
import dev.mvc.log.LogVO;
import dev.mvc.member.MemberProcInter;
import dev.mvc.member.MemberVO;
import dev.mvc.survey.Survey;
import dev.mvc.survey.SurveyVO;
import dev.mvc.surveyitem.ItemProcInter;
import dev.mvc.surveyitem.ItemVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RequestMapping(value = "/survey")
@Controller
public class SurveyCont {
  @Autowired
  @Qualifier("dev.mvc.survey.SurveyProc")
  private SurveyProcInter surveyProc;
  
  @Autowired
  @Qualifier("dev.mvc.surveyitem.ItemProc")
  private ItemProcInter itemProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 5;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 5;
  
  /** 페이징 목록 주소 */
  private String list_file_name = "/survey/list_by_surveyno_search_paging";
  
  @Autowired
  @Qualifier("dev.mvc.log.LogProc")
  private LogProcInter logProc;

  private void logAction(String action, String table, int memberno, String details, HttpServletRequest request, String is_success) {
      LogVO logVO = new LogVO();
      logVO.setMemberno(memberno);
      logVO.setTable_name(table);
      logVO.setAction(action);
      logVO.setDetails(details);
      logVO.setIp(getClientIp(request)); // IP 주소 설정
      logVO.setIs_success(is_success);
      logProc.create(logVO); // Log 테이블에 삽입
  }

  private String getClientIp(HttpServletRequest request) {
      String ip = request.getHeader("X-Forwarded-For");
      if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getHeader("Proxy-Client-IP");
      }
      if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getHeader("WL-Proxy-Client-IP");
      }
      if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getRemoteAddr();
      }
      return ip;
  }
  
  public SurveyCont() {
    System.out.println("-> SurveyCont created.");
  }
  
  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * 
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
      @RequestParam(name="url", defaultValue="") String url) {
//    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }
  
  /**
   * 등록
   * @param model
   * @return
   */
  @GetMapping(value = "/create")
  public String create(Model model,
      @ModelAttribute("SurveyVO") SurveyVO surveyVO,
      @RequestParam(name="memberno", defaultValue="0")int memberno) {
    
    surveyVO.setMemberno(1);
    model.addAttribute("surveyVO", surveyVO);
        
    return "/survey/create";
  }
  
  /**
   * 등록 처리
   * @param model
   * @param surveyVO
   * @param bindingResult
   * @return
   */
  @PostMapping(value = "/create")
  public String create(HttpServletRequest request, 
      HttpSession session, Model model, 
      @Valid @ModelAttribute("surveyVO") SurveyVO surveyVO, 
      BindingResult bindingResult, 
      RedirectAttributes ra) {
    
    int memberno = (int) session.getAttribute("memberno");
    if (memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      // ------------------------------------------------------------------------------
      // 파일 전송 코드 시작
      // ------------------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image
      String file1saved = ""; // 저장된 파일명, image
      String thumb1 = ""; // preview image

      String upDir = Survey.getUploadDir(); // 파일을 업로드할 폴더 준비
      System.out.println("-> upDir: " + upDir);
      MultipartFile mf = surveyVO.getFile1MF();

      file1 = mf.getOriginalFilename(); // 원본 파일명 산출, 01.jpg
      System.out.println("-> 원본 파일명 산출 file1: " + file1);

      long size1 = mf.getSize(); // 파일 크기
      if (size1 > 0) { // 파일 크기 체크, 파일을 올리는 경우
        if (Tool.checkUploadFile(file1) == true) { // 업로드 가능한 파일인지 검사
          // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg, spring_2.jpg...
          file1saved = Upload.saveFileSpring(mf, upDir);

          if (Tool.isImage(file1saved)) { // 이미지인지 검사
            // thumb 이미지 생성후 파일명 리턴됨, width: 200, height: 150
            thumb1 = Tool.preview(upDir, file1saved, 200, 150);
          }

          surveyVO.setFile1(file1); // 순수 원본 파일명
          surveyVO.setFile1saved(file1saved); // 저장된 파일명(파일명 중복 처리)
          surveyVO.setThumb1(thumb1); // 원본이미지 축소판
          surveyVO.setSize1(size1); // 파일 크기

        } else { // 전송 못하는 파일 형식
          ra.addFlashAttribute("code", "check_upload_file_fail"); // 업로드 할 수 없는 파일
          ra.addFlashAttribute("cnt", 0); // 업로드 실패
          ra.addFlashAttribute("url", "/survey/msg"); // msg.html, redirect parameter 적용
          return "redirect:/survey/msg"; // Post -> Get - param...
        }
      } else { // 글만 등록하는 경우
        System.out.println("-> 글만 등록");
      }
      // ------------------------------------------------------------------------------
      // 파일 전송 코드 종료
      // ------------------------------------------------------------------------------
      surveyVO.setMemberno(memberno);
      int cnt = this.surveyProc.create(surveyVO);

      if (cnt == 1) {
        logAction("create", "survey", memberno, "topic=" + surveyVO.getTopic(), request, "Y");  
        ra.addAttribute("memberno", surveyVO.getMemberno()); // controller -> controller: O
        return "redirect:/survey/list_by_surveyno_admin";

      } else {
        logAction("create", "survey", memberno, "topic=" + surveyVO.getTopic(), request, "N");  
        ra.addFlashAttribute("code", "create_fail"); // DBMS 등록 실패
        ra.addFlashAttribute("cnt", 0); // 업로드 실패
        ra.addFlashAttribute("url", "/survey/msg"); // msg.html, redirect parameter 적용
        return "redirect:/survey/msg"; // Post -> Get - param...
      }
    } else { // 로그인 실패 한 경우
      return "redirect:/member/login_cookie_need"; // /member/login_cookie_need.html
    }
  }
  
  /**
   * 유형 3
   * 카테고리별 목록 + 검색 + 페이징 
   * 회원
   * @return
   */
  @GetMapping(value = "/list_by_surveyno_search_paging")
  public String list_by_surveyno_search_paging(
      HttpSession session, 
      Model model, 
      @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
      @RequestParam(name = "is_continue", defaultValue = "") String is_continue,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    if (this.memberProc.isMember(session)) { // 회원 로그인한 경우
      System.out.println("surveyno: " + surveyno);
      model.addAttribute("surveyno", surveyno);

      int record_per_page = 5;
      int startRow = (now_page - 1) * record_per_page + 1;
      int endRow = now_page * record_per_page;

      int memberno = (int)session.getAttribute("memberno");
      MemberVO memberVO = this.memberProc.read(memberno);
      if (memberVO == null) {
          memberVO = new MemberVO();
          memberVO.setMemberno(1);
          model.addAttribute("message", "회원 정보가 없습니다.");
      }
      is_continue = Tool.checkNull(is_continue).trim();
      model.addAttribute("memberVO", memberVO);
      model.addAttribute("surveyno", surveyno);
      model.addAttribute("is_continue", is_continue);
      model.addAttribute("now_page", now_page);

      HashMap<String, Object> map = new HashMap<>();
      map.put("memberno", memberno);
      map.put("is_continue", is_continue);
      map.put("now_page", now_page);
      map.put("startRow", startRow);
      map.put("endRow", endRow);

      ArrayList<SurveyVO> list = this.surveyProc.list_by_surveyno_search_paging(map);
      model.addAttribute("list", list);

      int search_count = this.surveyProc.count_by_surveyno_search(map);
      String paging = this.surveyProc.pagingBox(memberno, now_page, is_continue, "/survey/list_by_surveyno_search_paging", search_count,
          Survey.RECORD_PER_PAGE, Survey.PAGE_PER_BLOCK);
      model.addAttribute("paging", paging);
//      model.addAttribute("is_continue", is_continue);
      model.addAttribute("now_page", now_page);
      model.addAttribute("search_count", search_count);


      int no = search_count - ((now_page - 1) * Survey.RECORD_PER_PAGE);
      model.addAttribute("no", no);

      return "/survey/list_by_surveyno_search_paging"; // /templates/board/list_by_boardno_search_paging.html
      } else {
      return "member/login_cookie_need";
      }

  }
  
  /**
   * 유형 3
   * 카테고리별 목록 + 검색 + 페이징 
   * 관리자 
   * @return
   */
  @GetMapping(value = "/list_by_surveyno_admin")
  public String list_by_surveyno_admin(
      HttpSession session, 
      Model model, 
      @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
      @RequestParam(name = "is_continue", defaultValue = "") String is_continue,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인한 경우

      int record_per_page = 5;
      int startRow = (now_page - 1) * record_per_page + 1;
      int endRow = now_page * record_per_page;

      int memberno = (int)session.getAttribute("memberno");
      MemberVO memberVO = this.memberProc.read(memberno);
      if (memberVO == null) {
          memberVO = new MemberVO();
          memberVO.setMemberno(1);
          model.addAttribute("message", "회원 정보가 없습니다.");
      }
      is_continue = Tool.checkNull(is_continue).trim();
      model.addAttribute("memberVO", memberVO);
//      model.addAttribute("surveyno", surveyno);
      model.addAttribute("is_continue", is_continue);
      model.addAttribute("now_page", now_page);

      HashMap<String, Object> map = new HashMap<>();
      map.put("memberno", memberno);
      map.put("is_continue", is_continue);
      map.put("now_page", now_page);
      map.put("startRow", startRow);
      map.put("endRow", endRow);

      ArrayList<SurveyVO> list = this.surveyProc.list_by_surveyno_search_paging(map);
      model.addAttribute("list", list);

      int search_count = this.surveyProc.count_by_surveyno_search(map);
      String paging = this.surveyProc.pagingBox(memberno, now_page, is_continue, "/survey/list_by_surveyno_admin", search_count,
          Survey.RECORD_PER_PAGE, Survey.PAGE_PER_BLOCK);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      model.addAttribute("search_count", search_count);


      int no = search_count - ((now_page - 1) * Survey.RECORD_PER_PAGE);
      model.addAttribute("no", no);

      return "/survey/list_by_surveyno_admin"; // /templates/board/list_by_boardno_search_paging.html
      } else {
      return "member/login_cookie_need";
      }

  }
  
  /**
   * 조회
   * @param model
   * @param surveyno
   * @param is_continue
   * @param now_page
   * @return
   */
//  @GetMapping(value = "/read")
//  public String read(HttpSession session, Model model, 
//      @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
//      @RequestParam(name = "is_continue", defaultValue = "0") String is_continue,
//      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
//    
//    if (this.memberProc.isMemberAdmin(session)) { // 관리자만 조회 가능
//    
//      SurveyVO surveyVO = this.surveyProc.read(surveyno);
//      model.addAttribute("surveyVO", surveyVO);
//  
//      long size1 = surveyVO.getSize1();
//      String size1_label = Tool.unit(size1);
//      surveyVO.setSize1_label(size1_label);
//      
//      MemberVO memberVO = this.memberProc.read(surveyVO.getMemberno());
//      model.addAttribute("memberVO", memberVO);
//      
//      // 조회에서 화면 하단에 출력
//      // ArrayList<ReplyVO> reply_list = this.replyProc.list_contents(contentsno);
//      // mav.addObject("reply_list", reply_list);
//      
//      model.addAttribute("now_page", now_page);
//  
//  
//      return "/survey/read";
//    } else {
//      return "redirect:/member/login_cookie_need";
//
//    }
//
//  }
  
  /**
   * 글 수정 폼
   *
   */
  @GetMapping(value = "/update_text")
  public String update_text(HttpSession session, 
      Model model, 
      RedirectAttributes ra, 
      @RequestParam(name="surveyno", defaultValue="") int surveyno) {

    model.addAttribute("surveyno", surveyno);

    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      SurveyVO surveyVO = this.surveyProc.read(surveyno);
      model.addAttribute("surveyVO", surveyVO);

      MemberVO memberVO = this.memberProc.read(surveyVO.getMemberno());
      model.addAttribute("memberVO", memberVO);

      return "/survey/update_text"; // /templates/contents/update_text.html
      // String content = "장소:\n인원:\n준비물:\n비용:\n기타:\n";
      // model.addAttribute("content", content);

    } else {
      return "member/login_cookie_need";
    }

  }

  /**
   * 글 수정 처리 
   * 
   * @return
   */
  @PostMapping(value = "/update_text")
  public String update_text(HttpSession session, 
      Model model, HttpServletRequest request, RedirectAttributes ra,
      @ModelAttribute("surveyVO") SurveyVO surveyVO) {

      int memberno = (int) session.getAttribute("memberno");
      
      if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
          HashMap<String, Object> map = new HashMap<>();
          map.put("surveyno", surveyVO.getSurveyno());

          this.surveyProc.update_text(surveyVO); // 글 수정 처리
          logAction("update_text", "survey", memberno, "topic=" + surveyVO.getTopic(), request, "Y");
          // Redirect 시 필요한 데이터 추가
          ra.addAttribute("surveyno", surveyVO.getSurveyno());
          ra.addAttribute("memberno", surveyVO.getMemberno());
          return "redirect:/survey/list_by_surveyno_admin"; // @GetMapping(value = "/read")

      } else { // 정상적인 로그인이 아닌 경우 로그인 유도
        logAction("update_text", "survey", memberno, "topic=" + surveyVO.getTopic(), request, "N");
          ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
          return "redirect:/survey/post2get"; // @GetMapping(value = "/msg")
      }
  }


  /**
   * 파일 수정 폼 
   * @return
   */
  @GetMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, 
      @RequestParam(name="surveyno", defaultValue="") int surveyno) {

    model.addAttribute("surveyno", surveyno);
    
    if (memberProc.isMemberAdmin(session)) {
      SurveyVO surveyVO = surveyProc.read(surveyno);
      model.addAttribute("surveyVO", surveyVO);

      MemberVO memberVO = memberProc.read(surveyVO.getMemberno());
      model.addAttribute("memberVO", memberVO);

      return "/survey/update_file";
    } else {
        return "member/login_cookie_need";
    }
  }
  
  /**
   * 파일 수정 처리 
   * @return
   */
  @PostMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, RedirectAttributes ra,HttpServletRequest request,
                            @ModelAttribute("surveyVO") SurveyVO surveyVO,
                            @RequestParam(name="surveyno", defaultValue="") int surveyno) {

    int memberno = (int) session.getAttribute("memberno");
    if (this.memberProc.isMemberAdmin(session)) {
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
      SurveyVO surveyVO_old = surveyProc.read(surveyVO.getSurveyno());

      String file1saved = surveyVO_old.getFile1saved(); // 실제 저장된 파일명
      String thumb1 = surveyVO_old.getThumb1(); // 실제 저장된 preview 이미지 파일명
      long size1 = 0;

      String upDir = Survey.getUploadDir(); // C:/kd/deploy/resort_v4sbm3c/contents/storage/

      Tool.deleteFile(upDir, file1saved); // 실제 저장된 파일삭제
      Tool.deleteFile(upDir, thumb1); // preview 이미지 삭제

      String file1 = ""; // 원본 파일명 image

      MultipartFile mf = surveyVO.getFile1MF();

      file1 = mf.getOriginalFilename(); // 원본 파일명
      size1 = mf.getSize(); // 파일 크기

      if (size1 > 0) { // 폼에서 새롭게 올리는 파일이 있는지 파일 크기로 체크 ★
        // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
        file1saved = Upload.saveFileSpring(mf, upDir);

        if (Tool.isImage(file1saved)) { // 이미지인지 검사
          // thumb 이미지 생성후 파일명 리턴됨, width: 250, height: 200
          thumb1 = Tool.preview(upDir, file1saved, 250, 200);
        }

      } else { // 파일이 삭제만 되고 새로 올리지 않는 경우
        file1 = "";
        file1saved = "";
        thumb1 = "";
        size1 = 0;
      }

      surveyVO.setFile1(file1);
      surveyVO.setFile1saved(file1saved);
      surveyVO.setThumb1(thumb1);
      surveyVO.setSize1(size1);
      // -------------------------------------------------------------------
      // 파일 전송 코드 종료
      // -------------------------------------------------------------------

      this.surveyProc.update_file(surveyVO); // Oracle 처리
      ra.addAttribute ("surveyno", surveyVO.getSurveyno());
      ra.addAttribute("memberno", surveyVO.getMemberno());
      logAction("update_file", "survey", memberno, "topic=" + surveyVO.getTopic(), request, "Y");
      return "redirect:/survey/list_by_surveyno_admin";
    } else {
      logAction("update_file", "survey", memberno, "topic=" + surveyVO.getTopic(), request, "N");
      ra.addAttribute("url", "/member/login_cookie_need"); 
      return "redirect:/survey/post2get"; // GET
    }
  }
  
  /**
   * 삭제폼 http://localhost:9091/cate/delete/1
   */
  @GetMapping(value = "/check_delete")
  public String delete(HttpSession session, Model model, 
      @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
                                   @RequestParam(name = "itemno", defaultValue = "1") int itemno) {
    if (this.memberProc.isMemberAdmin(session)) {
      
      model.addAttribute("surveyno", surveyno);
      
      int memberno = (int)session.getAttribute("memberno");
      model.addAttribute("memberno", memberno);
      System.out.println("memberno: " + memberno);

      SurveyVO surveyVO = this.surveyProc.read(surveyno);
      model.addAttribute("surveyVO", surveyVO);
      
      ItemVO itemVO = this.itemProc.read(itemno);
      model.addAttribute("itemVO", itemVO);     
            
      // 자식레코드 산출
      int cnt = this.surveyProc.cntcount(surveyno);
      
      if (cnt == 0) {
        // 콘텐츠가 없을 경우 cate/delete.html로 이동
        return "/survey/delete";
      } else {
        // 콘텐츠가 있을 경우 cate/list_all_delete.html로 이동
        ArrayList<ItemVO> list_item = itemProc.list_all_com(surveyno); // 해당 카테고리의 콘텐츠 리스트 불러오기
        model.addAttribute("list_item", list_item);
        model.addAttribute("cnt", cnt);
        
        return "/survey/list_all_delete"; // cate/list_all_delete.html로 이동
      }
    } else {
      return "redirect:/member/login_cookie_need"; // 관리자 권한 필요
    }
  }
  
  /**
   * 삭제 처리
   */
  @PostMapping(value = "/delete")
  public String deleteProcess(HttpSession session, Model model,
                              @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
                              RedirectAttributes ra) {
      // 관리자 권한 확인
      if (!this.memberProc.isMemberAdmin(session)) {
          return "redirect:/member/login_cookie_need";
      }

      SurveyVO surveyVO = this.surveyProc.read(surveyno);
      if (surveyVO == null) {
          return "redirect:/survey/list_by_surveyno_admin"; // 설문조사가 없는 경우 목록으로 이동
      }

      model.addAttribute("surveyVO", surveyVO);

      // 파일 삭제 로직
      String file1saved = surveyVO.getFile1saved();
      String thumb1 = surveyVO.getThumb1();
      String uploadDir = Survey.getUploadDir();

      if (file1saved != null && !file1saved.isEmpty()) {
          Tool.deleteFile(uploadDir, file1saved); // 실제 파일 삭제
      }

      if (thumb1 != null && !thumb1.isEmpty()) {
          Tool.deleteFile(uploadDir, thumb1); // 썸네일 삭제
      }

      // 삭제 처리
      int deleteCnt = this.surveyProc.delete(surveyno);
      System.out.println("-> deleteCnt: " + deleteCnt);

      // 삭제 완료 후 목록으로 리다이렉트
      ra.addAttribute("surveyno", surveyno);
      return "redirect:/survey/list_by_surveyno_admin";
  }


}

