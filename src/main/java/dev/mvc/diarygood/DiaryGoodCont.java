package dev.mvc.diarygood;

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
@RequestMapping(value="/diarygood")
public class DiaryGoodCont {
  
  @Autowired
  @Qualifier("dev.mvc.diarygood.DiaryGoodProc")
  DiaryGoodProcInter diaryGoodProc;

  public DiaryGoodCont() {
    System.out.println("-> DiaryGoodCont Creadted");
  }
  
  @ResponseBody
  @PostMapping(value="/create")
  public String create(HttpSession session, @RequestBody DiaryGoodVO diaryGoodVO) {
    System.out.println("-> 수신 데이터 : " + diaryGoodVO.toString());
    
    // test용
    int memberno = 1;
    
    //int memberno = (int) session.getAttribute("memberno");
    diaryGoodVO.setMemberno(memberno);
    
    int cnt = this.diaryGoodProc.create(diaryGoodVO);
    
    JSONObject json = new JSONObject();
    json.put("res", cnt);
    return json.toString();
  }
  
  @GetMapping(value="/list_all")
  public String list_all(Model model) {
    ArrayList<DiaryGoodVO> list = this.diaryGoodProc.list_all();
    model.addAttribute("list", list);
    return "/diarygood/list_all";
  }
  
  @PostMapping(value="/delete")
  public String delelete(Model model) {
    
    
    return "";
  }
  
}
