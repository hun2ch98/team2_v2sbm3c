package dev.mvc.survey;

import java.util.ArrayList;

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

import dev.mvc.survey.SurveyVO;
import jakarta.validation.Valid;

@RequestMapping(value = "/survey")
@Controller
public class SurveyCont {
  @Autowired
  @Qualifier("dev.mvc.survey.SurveyProc")
  private SurveyProcInter surveyProc;
  
  public SurveyCont() {
    System.out.println("-> SurveyCont created.");
  }
  
  /**
   * 등록
   * @param model
   * @return
   */
  @GetMapping(value = "/create")
  public String create(Model model) {
    SurveyVO surveyVO = new SurveyVO();
    model.addAttribute("surveyVO", surveyVO);
    
    surveyVO.setTopic("설문조사 주제");
    surveyVO.setSdate("시작 날짜를 알려주세요.");
    surveyVO.setEdate("종료 날짜를 알려주세요.");
    
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
  public String create(Model model, 
      @Valid @ModelAttribute("surveyVO") SurveyVO surveyVO, BindingResult bindingResult) {
    if (bindingResult.hasErrors() == true) { // 에러가 있으면 폼으로 돌아갈 것.
//    System.out.println("-> ERROR 발생");
    // model.addAttribute("cateVO", cateVO);
    return "/survey/create"; // /templates/cate/create.html
    }
    
    int cnt = this.surveyProc.create(surveyVO);
    System.out.println("-> cnt: " + cnt);

    if (cnt == 1) {
      // model.addAttribute("code", "create_success");
      // model.addAttribute("name", surveyVO.getName());

      // return "redirect:/cate/list_all"; // @GetMapping(value="/list_all")
      return "redirect:/survey/list_search"; // @GetMapping(value="/list_all")
    } else {
      model.addAttribute("code", "create_fail");
    }

    model.addAttribute("cnt", cnt);

    return "/survey/msg"; // /templates/survey/msg.html
    }
  
  /**
   * 전체 목록
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(Model model) {
    SurveyVO surveyVO = new SurveyVO();

    model.addAttribute("surveyVO", surveyVO);

    ArrayList<SurveyVO> list = this.surveyProc.list_all();
    model.addAttribute("list", list);

//    ArrayList<CateVO> menu = this.cateProc.list_all_categrp_y();
//    model.addAttribute("menu", menu);

//    ArrayList<SurveyVOMenu> menu = this.surveyProc.menu();
//    model.addAttribute("menu", menu);

    return "/survey/list_all"; // /templates/cate/list_all.html
  }
  
  /**
   * 조회
   * @param model
   * @param cateno
   * @param word
   * @param now_page
   * @return
   */
  @GetMapping(value = "/read/{surveyno}")
  public String read(Model model, @PathVariable("surveyno") int surveyno,
      @RequestParam(name = "now_page", defaultValue = "") int now_page) {
    SurveyVO surveyVO = this.surveyProc.read(surveyno);
    model.addAttribute("surveyVO", surveyVO);

    // ArrayList<CateVO> list = this.cateProc.list_all();
    // ArrayList<CateVO> list = this.cateProc.list_search(word);
//    ArrayList<CateVO> list = this.cateProc.list_search_paging(word, now_page, this.record_per_page);
//    model.addAttribute("list", list);

//   ArrayList<CateVO> menu = this.cateProc.list_all_categrp_y();
//   model.addAttribute("menu", menu);

//    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);

//    model.addAttribute("word", word);

    // --------------------------------------------------------------------------------------
    // 페이지 번호 목록 생성
    // --------------------------------------------------------------------------------------
//    int search_count = this.surveyProc.list_search_count(word);
//    String paging = this.surveyProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
//        this.page_per_block);
//    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
//    int no = search_count - ((now_page - 1) * this.record_per_page);
//    model.addAttribute("no", no);
    // --------------------------------------------------------------------------------------

    return "/survey/read";
  }



}
