package dev.mvc.surveyitem;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.member.MemberProcInter;
import dev.mvc.member.MemberVO;
import dev.mvc.survey.Survey;
import dev.mvc.survey.SurveyProcInter;
import dev.mvc.survey.SurveyVO;
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
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;

  /** 페이징 목록 주소 */
  private String list_file_name = "/cate/list_search";

  public ItemCont() {
    System.out.println("-> ItemCont created.");
  }

  /**
   * 설문조사 항목 추가 폼
   * @param surveyno
   * @param model
   * @return
   */
  @GetMapping(value = "/create")
  public String create(@RequestParam("surveyno") int surveyno, Model model) {

      ItemVO itemVO = new ItemVO();
      itemVO.setSurveyno(surveyno); // 설문조사 번호 설정
      model.addAttribute("itemVO", itemVO);

      return "/surveyitem/create";
  }


  /**
   * 설문조사 항목 추가 처리
   * @param itemVO
   * @param bindingResult
   * @param ra
   * @return
   */
  @PostMapping(value = "/create")
  public String create(@Valid @ModelAttribute("itemVO") ItemVO itemVO, 
                       BindingResult bindingResult, 
                       RedirectAttributes ra) {
    if (bindingResult.hasErrors()) {
      return "/surveyitem/create"; // 에러가 있으면 폼으로 복귀
    }

    int cnt = this.itemProc.create(itemVO); // 항목 추가
    if (cnt == 1) {
      ra.addAttribute("surveyno", itemVO.getSurveyno());
      return "redirect:/surveyitem/list_search"; // 성공 시 목록으로 이동
    } else {
      return "redirect:/surveyitem/msg"; // 실패 시 메시지
    }
  }
  
  /**
   * 회원
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

      if (this.memberProc.isMember(session)|| this.memberProc.isMemberAdmin(session)) {
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
      if (this.memberProc.isMemberAdmin(session)|| this.memberProc.isMember(session)) {
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

      if (this.memberProc.isMemberAdmin(session)|| this.memberProc.isMember(session)) { 
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
      if (this.memberProc.isMemberAdmin(session)|| this.memberProc.isMember(session)) {
          this.itemProc.delete(itemno); // 항목 삭제

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
      return "/surveyitem/finish"; // finish.html 템플릿으로 이동
  }

  
  /**
   * 설문조사 참여 처리
   * @return
   */
  @PostMapping("/finish")
  public String finish(@RequestParam("surveyno") int surveyno,
                            @RequestParam("itemno") int itemno,
                            HttpSession session,
                            RedirectAttributes ra) {
//       사용자 인증 확인
      if (session.getAttribute("memberno") == null) {
          ra.addFlashAttribute("msg", "로그인 후 참여 가능합니다.");
          return "redirect:/member/login";
      }

      // 설문 항목 참여 처리
      this.itemProc.update_cnt(itemno); // item_cnt 증가

      ra.addFlashAttribute("msg", "설문조사 완료!");
      return "redirect:/surveyitem/finish"; // 완료 페이지로 리다이렉트
  }


  /**
   * 등록 폼 및 검색 목록 + 페이징 
   * @return
   */
  @GetMapping(value = "/list_search")
  public String list_search_paging(HttpSession session, Model model,
                                   @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
                                   @RequestParam(name = "itemno", defaultValue = "0") int itemno,
                                   @RequestParam(name = "word", defaultValue = "") String word,
                                   @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

      if (this.memberProc.isMemberAdmin(session) || this.memberProc.isMember(session)) {
          word = Tool.checkNull(word);

          ArrayList<ItemVO> list = this.itemProc.list_search_paging(surveyno, word, now_page, this.record_per_page);
          model.addAttribute("list", list);

          int search_cnt = this.itemProc.count_by_search(word);
          model.addAttribute("search_cnt", search_cnt);

          model.addAttribute("word", word);
          model.addAttribute("surveyno", surveyno);
          model.addAttribute("itemno", itemno);

          String paging = this.itemProc.pagingBox(surveyno, now_page, word, this.list_file_name, search_cnt, 
                                                  this.record_per_page, this.page_per_block);
          model.addAttribute("paging", paging);
          model.addAttribute("now_page", now_page);

          int no = search_cnt - ((now_page - 1) * this.record_per_page);
          model.addAttribute("no", no);

          return "/surveyitem/list_search";
      } else {
          return "redirect:/member/login_cookie_need";
      }
  }


  
}
