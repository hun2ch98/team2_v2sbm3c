package dev.mvc.surveyitem;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.survey.SurveyProcInter;
import jakarta.validation.Valid;

@RequestMapping(value = "/surveyitem")
@Controller
public class ItemCont {
  
  @Autowired
  @Qualifier("dev.mvc.surveyitem.ItemProc")
  private ItemProcInter itemProc;
  
  @Autowired
  @Qualifier("dev.mvc.survey.SurveyProc")
  private SurveyProcInter surveyProc;
  
  public ItemCont() {
    System.out.println("-> ItemCont created.");
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
   * 등록 폼
   * @param model
   * @return
   */
  @GetMapping(value = "/create")
  public String create(Model model,
                      @RequestParam("surveyno") int surveyno) {
    ItemVO itemVO = new ItemVO();
    itemVO.setSurveyno(surveyno);
    model.addAttribute("itemVO", itemVO);

    itemVO.setItem("항목을 입력하세요.");
    return "/surveyitem/create"; // /templates/cate/create.html
  }
 
  /**
   * 등록 처리
   * @param model
   * @param itemVO
   * @param bindingResult
   * @return
   */
  @PostMapping(value = "/create")
  public String create(@Valid @ModelAttribute("itemVO") ItemVO itemVO, 
                        BindingResult bindingResult, 
                        RedirectAttributes ra) {
    if (bindingResult.hasErrors()) {
      return "/surveyitem/create"; // 에러가 있으면 폼으로 돌아감
    }

    int cnt = this.itemProc.create(itemVO);
    if (cnt == 1) {
      ra.addAttribute("surveyno", itemVO.getSurveyno());
      return "redirect:/surveyitem/list_all";
    } else {
      ra.addFlashAttribute("code", "create_fail");
      return "redirect:/surveyitem/msg";
    }
  }
  /**
   * 목록
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(@RequestParam("surveyno") int surveyno,
                    Model model) {
    ItemVO itemVO = new ItemVO();
    model.addAttribute("itemVO", itemVO);

    ArrayList<ItemVO> list = this.itemProc.list_all();
    model.addAttribute("list", list);
    model.addAttribute("surveyno", surveyno);

    return "/surveyitem/list_all"; // /templates/cate/list_all.html
  }

}
