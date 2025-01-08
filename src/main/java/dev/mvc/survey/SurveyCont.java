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
import dev.mvc.surveyitem.ItemProcInter;
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
  public int record_per_page = 7;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 7;
  
  /** 페이징 목록 주소 */
  private String list_file_name = "/survey/list_by_surveyno_search_paging";
  
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
    
//    if (memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
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
//    } else { // 로그인 실패 한 경우
//      return "redirect:/member/login_cookie_need"; // /member/login_cookie_need.html
//    }
  }
  
  /**
   * 전체 목록
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(HttpSession session, Model model) {
    
//    if (this.memberProc.isMemberAdmin(session)) { // 관리자만 조회 가능
  
      ArrayList<SurveyVO> list = this.surveyProc.list_all();
      model.addAttribute("list", list);
  
      return "/survey/list_all"; // /templates/cate/list_all.html
//      } else {
//        return "redirect:/member/login_cookie_need";
//  
//      }
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
    if (this.memberProc.isMember(session) || this.memberProc.isMemberAdmin(session)) { // 회원 또는 관리자 로그인한 경우

      int record_per_page = 10;
      int startRow = (now_page - 1) * record_per_page + 1;
      int endRow = now_page * record_per_page;

//      int memberno = 1;
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
      model.addAttribute("is_continue", is_continue);
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
  @GetMapping(value = "/update_text/{surveyno}")
  public String update_text(HttpSession session, 
      Model model, 
      @PathVariable("surveyno") int surveyno,
      RedirectAttributes ra, 
      @RequestParam(name="is_continue", defaultValue="") String is_continue,
      @RequestParam(name="now_page", defaultValue="1") int now_page) {

    model.addAttribute("is_continue", is_continue);
    model.addAttribute("now_page", now_page);
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
  @PostMapping(value = "/update_text/{surveyno}")
  public String update_text(HttpSession session, 
      Model model, 
      @ModelAttribute("surveyVO") SurveyVO surveyVO, 
      @PathVariable("surveyno") int surveyno,
      RedirectAttributes ra,
      @RequestParam(name = "is_continue", defaultValue = "") String is_continue,
      @RequestParam(name = "now_page", defaultValue = "0") int now_page) {

      // Redirect 시 검색어 및 현재 페이지 유지
      ra.addAttribute("is_continue", is_continue);
      ra.addAttribute("now_page", now_page);

      if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
          HashMap<String, Object> map = new HashMap<>();
          map.put("surveyno", surveyVO.getSurveyno());

          this.surveyProc.update_text(surveyVO); // 글 수정 처리

          // Redirect 시 필요한 데이터 추가
          ra.addAttribute("surveyno", surveyVO.getSurveyno());
          ra.addAttribute("memberno", surveyVO.getMemberno());
          return "redirect:/survey/list_by_surveyno_search_paging"; // @GetMapping(value = "/read")

      } else { // 정상적인 로그인이 아닌 경우 로그인 유도
          ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
          return "redirect:/survey/post2get"; // @GetMapping(value = "/msg")
      }
  }


  /**
   * 파일 수정 폼 
   * @return
   */
  @GetMapping(value = "/update_file/{surveyno}")
  public String update_file(HttpSession session, Model model, 
         @PathVariable("surveyno") int surveyno,
         @RequestParam(name="is_continue", defaultValue="") String is_continue, 
         @RequestParam(name="now_page", defaultValue="1") int now_page) {
    
    model.addAttribute("is_continue", is_continue);
    model.addAttribute("now_page", now_page);
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
  @PostMapping(value = "/update_file/{surveyno}")
  public String update_file(HttpSession session, Model model, RedirectAttributes ra,
                            @ModelAttribute("surveyVO") SurveyVO surveyVO,
                            @PathVariable("surveyno") int surveyno,
                            @RequestParam(name="is_continue", defaultValue="") String is_continue, 
                            @RequestParam(name="now_page", defaultValue="1") int now_page) {

    if (this.memberProc.isMemberAdmin(session)) {
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
      SurveyVO surveyVO_old = surveyProc.read(surveyVO.getSurveyno());

      // -------------------------------------------------------------------
      // 파일 삭제 시작
      // -------------------------------------------------------------------
      String file1saved = surveyVO_old.getFile1saved(); // 실제 저장된 파일명
      String thumb1 = surveyVO_old.getThumb1(); // 실제 저장된 preview 이미지 파일명
      long size1 = 0;

      String upDir = Survey.getUploadDir(); // C:/kd/deploy/resort_v4sbm3c/contents/storage/

      Tool.deleteFile(upDir, file1saved); // 실제 저장된 파일삭제
      Tool.deleteFile(upDir, thumb1); // preview 이미지 삭제
      // -------------------------------------------------------------------
      // 파일 삭제 종료
      // -------------------------------------------------------------------

      // -------------------------------------------------------------------
      // 파일 전송 시작
      // -------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
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
      ra.addAttribute("is_continue", is_continue);
      ra.addAttribute("now_page", now_page);
      
      return "redirect:/survey/list_by_surveyno_search_paging";
    } else {
      ra.addAttribute("url", "/member/login_cookie_need"); 
      return "redirect:/survey/post2get"; // GET
    }
  }

  /**
   * 파일 삭제 폼
   * http://localhost:9091/contents/delete?contentsno=1
   * 
   * @return
   */
  @GetMapping(value = "/delete/{surveyno}")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,
                               @RequestParam(name="memberno", defaultValue="1") int memberno, 
                               @PathVariable("surveyno") int surveyno,
                               @RequestParam(name="is_continue", defaultValue="") String is_continue, 
                               @RequestParam(name="now_page", defaultValue="1") int now_page) {
    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      model.addAttribute("memberno", memberno);
      model.addAttribute("surveyno", surveyno);
      model.addAttribute("is_continue", is_continue);
      model.addAttribute("now_page", now_page);
      
      SurveyVO surveyVO = this.surveyProc.read(surveyno);
      model.addAttribute("surveyVO", surveyVO);
      
      MemberVO memberVO = this.memberProc.read(surveyVO.getMemberno());
      model.addAttribute("memberVO", memberVO);
      
      return "/survey/delete"; // forward
      
    } else {
      ra.addAttribute("url", "/admin/login_cookie_need");
      return "redirect:/survey/msg"; 
    }

  }
  
  /**
   * 카테고리 및 연관 자료 삭제 처리
   */
  @PostMapping(value = "/delete_all_confirm")
  public String delete_survey(@RequestParam (name="surveyno", defaultValue="0") int surveyno,
                                                       RedirectAttributes redirectAttributes) {
    // 콘텐츠 삭제
    itemProc.delete(surveyno);

    // 카테고리 삭제
    surveyProc.delete_survey(surveyno);

    redirectAttributes.addFlashAttribute("msg", "카테고리와 관련된 모든 자료가 삭제되었습니다.");
    return "redirect:/survey/list_by_surveyno_search_paging";
  }

  /**
   * 카테고리 삭제 폼
   */
  @GetMapping(value = "/delete")
  public String delete(Model model) {
    // 기본 삭제 폼
    return "/survey/delete";  // cate/delete.html로 이동
  }
  
  /**
   * 삭제 처리 http://localhost:9091/contents/delete
   * 
   * @return
   */
  @PostMapping(value = "/delete/{surveyno}")
  public String delete(HttpSession session, RedirectAttributes ra,
      @RequestParam(name="memberno", defaultValue="1") int memberno, 
      @RequestParam(name="surveyno", defaultValue="1") int surveyno, 
//      @PathVariable("surveyno") int surveyno,
      @RequestParam(name="is_continue", defaultValue="") String is_continue, 
      @RequestParam(name="now_page", defaultValue="1") int now_page) {
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      // -------------------------------------------------------------------
      // 파일 삭제 시작
      // -------------------------------------------------------------------
      // 삭제할 파일 정보를 읽어옴.
      SurveyVO surveyVO_read = surveyProc.read(surveyno);
          
      String file1saved = surveyVO_read.getFile1saved();
      String thumb1 = surveyVO_read.getThumb1();
      
      String uploadDir = Survey.getUploadDir();
      Tool.deleteFile(uploadDir, file1saved);  // 실제 저장된 파일삭제
      Tool.deleteFile(uploadDir, thumb1);     // preview 이미지 삭제
      // -------------------------------------------------------------------
      // 파일 삭제 종료
      // -------------------------------------------------------------------
          
      
//    --------------------------------------------------------------------------------------
//    자식 삭제
//  --------------------------------------------------------------------------------------
 
//      int cnt = this.surveyProc.cntcount(surveyno);
//      
//      if (cnt == 0) {
//        int deleteCnt = this.surveyProc.delete(surveyno);
//        System.out.println("-> deleteCnt: " + deleteCnt);
//        
//        if(deleteCnt == 1) {
//          ra.addAttribute("is_continue", is_continue);
//          
////          int search_cnt = this.surveyProc.count_by_surveyno_search(is_continue);
//          if(search_cnt % this.record_per_page == 0) {
//            now_page = now_page - 1;
//            if(now_page < 1) {
//              now_page = 1;
//            }
//          }
//          ra.addAttribute("now_page", now_page);
//        }
//      }
      
      this.surveyProc.delete(surveyno); // DBMS 삭제
          
      // -------------------------------------------------------------------------------------
      // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
      // -------------------------------------------------------------------------------------    
      // 마지막 페이지의 마지막 10번째 레코드를 삭제후
      // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
      // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생
      
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("memberno", memberno);
      map.put("is_continue", is_continue);
      
      if (this.surveyProc.count_by_surveyno_search(map) % Survey.RECORD_PER_PAGE == 0) {
        now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
        if (now_page < 1) {
          now_page = 1; // 시작 페이지
        }
      }
      // -------------------------------------------------------------------------------------   
      
      ra.addAttribute("memberno", memberno);
      
      
      ra.addAttribute("surveyno", surveyno);
      
      return "redirect:/survey/list_by_surveyno_search_paging";    
      
    }else {
      ra.addAttribute("url", "/admin/login_cookie_need");
      return "redirect:/survey/msg"; 
    }   
  
  } 
}

