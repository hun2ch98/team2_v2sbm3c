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
      System.out.println("Received surveyno in create: " + surveyno);

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
      return "redirect:/surveyitem/list_all_com"; // 성공 시 목록으로 이동
    } else {
      return "redirect:/surveyitem/msg"; // 실패 시 메시지
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
      System.out.println("Surveyno received: " + surveyno);

      // 데이터 가져오기
      ArrayList<ItemVO> list = this.itemProc.list_all_com(surveyno);

      // 디버깅: 리스트 데이터 확인
      if (list != null && !list.isEmpty()) {
          System.out.println("Retrieved list size: " + list.size());
          for (ItemVO item : list) {
              System.out.println(item);
          }
      } else {
          System.out.println("No items found for surveyno: " + surveyno);
      }

      model.addAttribute("list", list);
      model.addAttribute("surveyno", surveyno);

      return "/surveyitem/list_all_com";
  }
  
  /**
   * 설문조사 항목 수정 폼
   */
  @GetMapping(value = "/update/{itemno}")
  public String updateForm(HttpSession session, 
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
          return "redirect:/surveyitem/list_all_com";
      } else {
          return "member/login_cookie_need";
      }
  }

  
  /**
   * 설문조사 항목 삭제 폼
   */
  @GetMapping(value = "/delete/{itemno}")
  public String deleteForm(HttpSession session, 
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
          this.itemProc.delete(itemno); // 항목 삭제

          // Redirect 시 surveyno 값 추가
          ra.addAttribute("surveyno", surveyno);
          return "redirect:/surveyitem/list_all_com";
      } else {
          return "member/login_cookie_need";
      }
  }

  
}
