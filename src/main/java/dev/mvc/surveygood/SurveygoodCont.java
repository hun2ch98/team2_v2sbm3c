package dev.mvc.surveygood;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/surveygood")
public class SurveygoodCont {
  @Autowired
  @Qualifier("dev.mvc.surveygood.SurveygoodProc")
  SurveygoodProcInter surveygoodProc;
  
  public SurveygoodCont() {
    System.out.println("-> SurveygoodCont 생성됨.");
  }
  
  @PostMapping(value="/create")
  @ResponseBody
  public String create(HttpSession session, @RequestBody SurveygoodVO surveygoodVO) {
    System.out.println("-> 수신 데이터: " + surveygoodVO.toString());
    
    int memberno = 3;
//    int memberno = (int)session.getAttribute("memberno");
    surveygoodVO.setMemberno(memberno);
    
    int cnt = this.surveygoodProc.create(surveygoodVO);
    
    JSONObject json = new JSONObject();
    json.put("res", cnt);
    
    return json.toString();
  }
  
  /**
   * 목록
   * @param model
   * @return
   */
  // http://localhost:9091/cate/list_all
  @GetMapping(value = "/list_all")
  public String list_all(Model model) {
    ArrayList<SurveygoodVO> list = this.surveygoodProc.list_all();
    model.addAttribute("list", list);

    return "/surveygood/list_all"; // /templates/calendar/list_all.html
  }

}
