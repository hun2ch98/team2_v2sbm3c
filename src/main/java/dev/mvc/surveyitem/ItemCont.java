package dev.mvc.surveyitem;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.member.MemberProcInter;
import dev.mvc.member.MemberVO;
import dev.mvc.survey.Survey;
import dev.mvc.survey.SurveyProcInter;
import dev.mvc.survey.SurveyVO;
import dev.mvc.surveygood.SurveygoodProcInter;
import dev.mvc.surveygood.SurveygoodVO;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RequestMapping(value = "/surveyitem")
@Controller
public class ItemCont {

  @Autowired
  @Qualifier("dev.mvc.surveyitem.ItemProc")
  private ItemProcInter itemProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") 
  private MemberProcInter memberProc;

  @Autowired
  @Qualifier("dev.mvc.survey.SurveyProc")
  private SurveyProcInter surveyProc;
  
  @Autowired
  @Qualifier("dev.mvc.surveygood.SurveygoodProc")
  SurveygoodProcInter surveygoodProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 5;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 5;

  /** 페이징 목록 주소 */
  private String list_file_name = "/surveyitem/list_search";

  public ItemCont() {
    System.out.println("-> ItemCont created.");
  }

  /**
   * 설문조사 항목 추가 폼
   * @param surveyno
   * @param model
   * @return
   */
    @GetMapping(value = "/create/{surveyno}")
    public String create(Model model,
        @ModelAttribute("ItemVO") ItemVO itemVO,
        @PathVariable("surveyno")int surveyno) {
      SurveyVO surveyVO = this.surveyProc.read(surveyno);
      model.addAttribute("surveyVO", surveyVO);
      
      return "/surveyitem/create";
    }


  /**
   * 설문조사 항목 추가 처리
   * @return
   */
  @PostMapping(value = "/create")
  public String create(
      @ModelAttribute("itemVO") ItemVO itemVO,
      BindingResult bindingResult,
      RedirectAttributes ra,
      HttpSession session) {
//    System.out.println("surveyno: " + surveyno);
    if (memberProc.isMemberAdmin(session)) { 
      if (bindingResult.hasErrors()) {
        return "/surveyitem/create";
      }

//      itemVO.setSurveyno(surveyno); // 경로 변수에서 가져온 surveyno 설정
      int cnt = this.itemProc.create(itemVO); // 항목 추가

      if (cnt == 1) {
        ra.addAttribute("surveyno", itemVO.getSurveyno());
        return "redirect:/surveyitem/list_search"; // 성공 시 목록으로 이동
      } else {
        return "redirect:/surveyitem/msg"; // 실패 시 메시지
      }
    } else { // 로그인 실패 한 경우
      return "redirect:/member/login_cookie_need";
    }
  }

  
  /**
   * 설문조사 항목 목록 보기
   * @param surveyno
   * @param model
   * @return
   */
  @GetMapping("/list_all_com")
  public String list_all_com(@RequestParam("surveyno") int surveyno, Model model) {

      // 데이터 가져오기
      ArrayList<ItemVO> list = this.itemProc.list_all_com(surveyno);

      // 디버깅: 리스트 데이터 확인
      if (list != null && !list.isEmpty()) {
          for (ItemVO item : list) {
          }
      } else {
      }

      model.addAttribute("list", list);
      model.addAttribute("surveyno", surveyno);

      return "/surveyitem/list_search";
  }
  
  /**
   * 설문조사 항목 수정 폼
   */
  @GetMapping(value = "/update/{itemno}")
  public String update(HttpSession session, 
                           Model model, 
                           @PathVariable("itemno") int itemno, 
                           RedirectAttributes ra) {

      if (this.memberProc.isMemberAdmin(session)) {
          ItemVO itemVO = this.itemProc.read(itemno);
          if (itemVO == null) {
              ra.addFlashAttribute("msg", "잘못된 항목 번호입니다.");
              return "redirect:/surveyitem/msg";
          }

          model.addAttribute("itemVO", itemVO);
          return "/surveyitem/update";
      } else {
          return "member/login_cookie_need";
      }
  }

  /**
   * 설문조사 항목 수정 처리
   */
  @PostMapping(value = "/update/{itemno}")
  public String update(HttpSession session, 
                       @PathVariable("itemno") int itemno,
                       @ModelAttribute("itemVO") ItemVO itemVO, 
                       RedirectAttributes ra) {
      if (this.memberProc.isMemberAdmin(session)) {
          itemVO.setItemno(itemno); // URL에서 받은 itemno 설정
          this.itemProc.update(itemVO); // 항목 수정 처리

          // Redirect 시 surveyno 값 추가
          ra.addAttribute("surveyno", itemVO.getSurveyno());
          return "redirect:/surveyitem/list_search";
      } else {
          return "member/login_cookie_need";
      }
  }

  
  /**
   * 설문조사 항목 삭제 폼
   */
  @GetMapping(value = "/delete/{itemno}")
  public String delete(HttpSession session, 
                           Model model, 
                           @PathVariable("itemno") int itemno,
                           RedirectAttributes ra) {

      if (this.memberProc.isMemberAdmin(session)) { 
        
        ItemVO itemVO = this.itemProc.read(itemno);
        if (itemVO == null) {
            ra.addFlashAttribute("msg", "잘못된 항목 번호입니다.");
            return "redirect:/surveyitem/msg";
        }

        model.addAttribute("itemVO", itemVO);
          return "/surveyitem/delete";
      } else {
          return "member/login_cookie_need";
      }
  }

  /**
   * 설문조사 항목 삭제 처리
   */
  @PostMapping(value = "/delete/{itemno}")
  public String delete(HttpSession session, 
                       @PathVariable("itemno") int itemno, 
                       @RequestParam("surveyno") int surveyno, // surveyno 값 추가
                       RedirectAttributes ra) {
      if (this.memberProc.isMemberAdmin(session)) {
          this.itemProc.delete(itemno);// 항목 삭제
          
          HashMap<String, Object> map = new HashMap<String, Object>();
          map.put("surveyno", surveyno);

          // Redirect 시 surveyno 값 추가
          ra.addAttribute("surveyno", surveyno);
          return "redirect:/surveyitem/list_search";
      } else {
          return "member/login_cookie_need";
      }
  }
  
  /**
   * 설문조사
   * @return
   */
  @GetMapping("/finish")
  public String finish(Model model) {
      model.addAttribute("message", "설문조사가 완료되었습니다.");
      return "/surveyitem/finish"; // finish.html 템플릿
  }

  
  /**
   * 설문조사 참여 처리
   * @return
   */
  @PostMapping("/finish")
  public String finish(
      @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
      @RequestParam("itemno") int itemno,
      HttpSession session,
      RedirectAttributes ra) {
      if (session.getAttribute("memberno") == null) {
          ra.addFlashAttribute("msg", "로그인 후 참여 가능합니다.");
          return "redirect:/member/login";
      }

      this.itemProc.update_cnt(itemno); // item_cnt 증가
      ra.addFlashAttribute("msg", "설문조사 완료!");
      return "redirect:/surveyitem/finish";
  }



  /**
   * 등록 폼 및 검색 목록 + 페이징
   * @return
   */
  @GetMapping(value = "/list_search")
  public String list_search_paging(HttpSession session, Model model,
                                  @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
                                  @RequestParam(name = "word", defaultValue = "") String word,
                                  @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    
      model.addAttribute("surveyno", surveyno);
      
      // 관리자 또는 일반 회원인지 확인
      if (this.memberProc.isMember(session)) {
        SurveyVO surveyVO = this.surveyProc.read(surveyno);
        model.addAttribute("surveyVO", surveyVO);

        word = Tool.checkNull(word);

        ArrayList<ItemVO> list = this.itemProc.list_search_paging(surveyno, word, now_page, this.record_per_page);
//        System.out.println("-> listsize: " + list.size());
        model.addAttribute("list", list);
        
        // --------------------------------------------------------------------------------------
        // 페이지 번호 목록 생성
        // --------------------------------------------------------------------------------------
        int search_count = this.itemProc.count_by_search(word);
//        System.out.println("->search_count : " + search_count);
        String paging = this.itemProc.pagingBox(surveyno, now_page, word, this.list_file_name, search_count, this.record_per_page,
            this.page_per_block);
        model.addAttribute("paging", paging);
        model.addAttribute("now_page", now_page);
        model.addAttribute("search_count", search_count);
        model.addAttribute("word", word);

        // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
        int no = search_count - ((now_page - 1) * this.record_per_page);
        model.addAttribute("no", no);
        // --------------------------------------------------------------------------------------
        
        
//      -------------------------------------------------------------------
//      추천 관련
//      -------------------------------------------------------------------
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("surveyno", surveyno);
        
        int heartCnt = 0;
        if(session.getAttribute("memberno") != null) {  // 회원인 경우만 카운트 처리
          int memberno = (int)session.getAttribute("memberno");
          map.put("memberno", memberno);
          
          heartCnt = this.surveygoodProc.heartCnt(map);
        } 
        
        
        model.addAttribute("heartCnt", heartCnt);
//    -------------------------------------------------------------------

        return "/surveyitem/list_search";
      } else {
          return "redirect:/member/login_cookie_need";
      }
  }
  
  /**
   * 추천 처리 http://localhost:9093/surveyitem/good
   * 
   * @return
   */
  @PostMapping(value = "/good")
  @ResponseBody
  public String update_text(HttpSession session,
      Model model, @RequestBody String json_src) {
    System.out.println("-> json_src: " + json_src); // json_src: {"surveyno":"12"}
    
    JSONObject src = new JSONObject(json_src); // String -> JSON
    int surveyno = (int)src.get("surveyno"); // 값 가져오기
    System.out.println("-> surveyno: " + surveyno);
    
   
    if (this.memberProc.isMember(session)) { // 회원 로그인 확인
      // 추천을 한 상태인지 확인
      int memberno = (int)session.getAttribute("memberno");
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("surveyno", surveyno);
      map.put("memberno", memberno);
      
      int good_cnt = this.surveygoodProc.heartCnt(map);
      System.out.println("-> good_cnt: " + good_cnt);
      
      if(good_cnt == 1) {
        System.out.println("-> 추천 해제: " + surveyno + ' ' + memberno);
        SurveygoodVO surveygoodVO = this.surveygoodProc.read(map);
        
        this.surveygoodProc.delete(surveygoodVO.getGoodno());  // 추천 삭제
        this.surveyProc.decreasegoodcnt(surveyno);    // 카운트 감소
        
      }else {
        System.out.println("-> 추천: " + surveyno + ' ' + memberno);
        
        SurveygoodVO surveygoodVO_new = new SurveygoodVO();
        surveygoodVO_new.setSurveyno(surveyno);
        surveygoodVO_new.setMemberno(memberno);
        
        this.surveygoodProc.create(surveygoodVO_new);
        this.surveyProc.increasegoodcnt(surveyno);
      }
      
      // 추천 여부가 변경되어 다시 새로운 값을 읽어옴
      int heartCnt = this.surveygoodProc.heartCnt(map);
      int goodcnt = this.surveyProc.read(surveyno).getGoodcnt();
      
      JSONObject result = new JSONObject();
      result.put("isMember", 1);  // 로그인:1, 비회원:0
      result.put("heartCnt", heartCnt);  // 추천 여부, 추천:1, 비추천:0
      result.put("goodcnt", goodcnt);   // 추천인수
      
      return result.toString();

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      JSONObject result = new JSONObject();
      result.put("isMember", 1);  // 로그인:1, 비회원:0
      
      return result.toString();
    }

  }  
}
