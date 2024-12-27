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

import dev.mvc.member.MemberProcInter;
import dev.mvc.member.MemberVO;
import dev.mvc.survey.Survey;
import dev.mvc.survey.SurveyVO;
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
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;
  
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
    
    if (memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
   // ------------------------------------------------------------------------------
      // 파일 전송 코드 시작
      // ------------------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image
      String file1saved = ""; // 저장된 파일명, image
      String thumb1 = ""; // preview image

      String upDir = Survey.getUploadDir(); // 파일을 업로드할 폴더 준비
      // upDir = upDir + "/" + 한글을 제외한 카테고리 이름
      System.out.println("-> upDir: " + upDir);

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
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

      // Call By Reference: 메모리 공유, Hashcode 전달
      int memberno = 1; // memberno FK
      surveyVO.setMemberno(memberno);
      int cnt = this.surveyProc.create(surveyVO);

      // ------------------------------------------------------------------------------
      // PK의 return
      // ------------------------------------------------------------------------------
      // System.out.println("--> contentsno: " + contentsVO.getContentsno());
      // mav.addObject("contentsno", contentsVO.getContentsno()); // redirect
      // parameter 적용
      // ------------------------------------------------------------------------------

      if (cnt == 1) {
        // type 1, 재업로드 발생
        // return "<h1>파일 업로드 성공</h1>"; // 연속 파일 업로드 발생

        // type 2, 재업로드 발생
        // model.addAttribute("cnt", cnt);
        // model.addAttribute("code", "create_success");
        // return "contents/msg";

        // type 3 권장
        // return "redirect:/contents/list_all"; // /templates/contents/list_all.html

        // System.out.println("-> contentsVO.getCateno(): " + contentsVO.getCateno());
        // ra.addFlashAttribute("cateno", contentsVO.getCateno()); // controller ->
        // controller: X

        ra.addAttribute("memberno", surveyVO.getMemberno()); // controller -> controller: O
        return "redirect:/survey/list_by_surveyno_search_paging";

        // return "redirect:/contents/list_by_cateno?cateno=" + contentsVO.getCateno();
        // // /templates/contents/list_by_cateno.html
      } else {
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
   * 전체 목록
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(HttpSession session, Model model) {
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자만 조회 가능
  
      ArrayList<SurveyVO> list = this.surveyProc.list_all();
      model.addAttribute("list", list);
  
      return "/survey/list_all"; // /templates/cate/list_all.html
      } else {
        return "redirect:/member/login_cookie_need";
  
      }
    }
  
  /**
   * 유형 3
   * 카테고리별 목록 + 검색 + 페이징  
   * @return
   */
  @GetMapping(value = "/list_by_surveyno_search_paging")
  public String list_by_surveyno_search_paging(
      HttpSession session, 
      Model model, 
      @ModelAttribute("surveyVO") SurveyVO surveyVO,
      @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
      @RequestParam(name = "is_continue", defaultValue = "") String is_continue,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

      int record_per_page = 10;
      int startRow = (now_page - 1) * record_per_page + 1;
      int endRow = now_page * record_per_page;

      int memberno = 1;
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
      model.addAttribute("is_continue", is_continue);
      model.addAttribute("now_page", now_page);
      model.addAttribute("search_count", search_count);


      int no = search_count - ((now_page - 1) * Survey.RECORD_PER_PAGE);
      model.addAttribute("no", no);

      return "/survey/list_by_surveyno_search_paging"; // /templates/board/list_by_boardno_search_paging.html
  }

  /**
   * 카테고리별 목록 + 검색 + 페이징 + Grid
   * @return
   */
//  @GetMapping(value = "/list_by_surveyno_search_paging_grid")
//  public String list_by_surveyno_search_paging_grid(HttpSession session, 
//      Model model, 
//      @RequestParam(name = "memberno", defaultValue = "1") int memberno,
//      @RequestParam(name = "is_continue", defaultValue = "1") String is_continue,
//      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
//
//
////  ArrayList<BoardVOMenu> menu = this.boardProc.menu();
////  model.addAttribute("menu", menu);
//
//    MemberVO memberVO = this.memberProc.read(memberno);
//    model.addAttribute("memberVO", memberVO);
//
//    is_continue = Tool.checkNull(is_continue).trim();
//
//    HashMap<String, Object> map = new HashMap<>();
//    map.put("memberno", memberno);
//    map.put("is_continue", is_continue);
//    map.put("now_page", now_page);
//
//    ArrayList<SurveyVO> list = this.surveyProc.list_by_surveyno_search_paging(map);
//    model.addAttribute("list", list);
//    
//    model.addAttribute("is_continue", is_continue);
//
//    int search_count = this.surveyProc.count_by_surveyno_search(map);
//    String paging = this.surveyProc.pagingBox(memberno, now_page, is_continue, "/survey/list_by_surveyno_search_paging", search_count,
//        Survey.RECORD_PER_PAGE, Survey.PAGE_PER_BLOCK);
//    model.addAttribute("paging", paging);
//    model.addAttribute("now_page", now_page);
//
//    model.addAttribute("search_count", search_count);
//
//    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
//    int no = search_count - ((now_page - 1) * Survey.RECORD_PER_PAGE);
//    model.addAttribute("no", no);
//
//    // /templates/contents/list_by_cateno_search_paging_grid.html
//    return "/survey/list_by_surveyno_search_paging_grid";
//  }
  
  /**
   * 조회
   * @param model
   * @param surveyno
   * @param is_continue
   * @param now_page
   * @return
   */
  @GetMapping(value = "/read")
  public String read(HttpSession session, Model model, 
      @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
      @RequestParam(name = "is_continue", defaultValue = "0") String is_continue,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자만 조회 가능
    
      SurveyVO surveyVO = this.surveyProc.read(surveyno);
      model.addAttribute("surveyVO", surveyVO);
  
      long size1 = surveyVO.getSize1();
      String size1_label = Tool.unit(size1);
      surveyVO.setSize1_label(size1_label);
      
      MemberVO memberVO = this.memberProc.read(surveyVO.getMemberno());
      model.addAttribute("memberVO", memberVO);
      
      // 조회에서 화면 하단에 출력
      // ArrayList<ReplyVO> reply_list = this.replyProc.list_contents(contentsno);
      // mav.addObject("reply_list", reply_list);
      
      model.addAttribute("now_page", now_page);
  
  
      return "/survey/read";
    } else {
      return "redirect:/member/login_cookie_need";

    }

  }
  

}