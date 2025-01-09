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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.member.MemberProcInter;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value="/diarygood")
public class DiaryGoodCont {
  
  @Autowired
  @Qualifier("dev.mvc.diarygood.DiaryGoodProc")
  DiaryGoodProcInter diaryGoodProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  MemberProcInter memberProc;

  public DiaryGoodCont() {
    System.out.println("-> DiaryGoodCont Creadted");
  }
  
  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * 
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
      @RequestParam(name="url", defaultValue = "") String url) {

    return url; // forward, /templates/...
  }
  
  @ResponseBody
  @PostMapping(value="/create")
  public String create(HttpSession session, @RequestBody DiaryGoodVO diaryGoodVO) {
    System.out.println("-> 수신 데이터 : " + diaryGoodVO.toString());
    
    // test용
    int memberno = 1;
    
    //int memberno = (int) session.getAttribute("memberno");
    diaryGoodVO.setMemberno(memberno);
    
    int goodcnt = this.diaryGoodProc.create(diaryGoodVO);
    
    JSONObject json = new JSONObject();
    json.put("res", goodcnt);
    return json.toString();
  }
  
  
//  @GetMapping(value="/read")
//  public String read(Model model, @RequestParam(name="goodno", defaultValue="0") int goodno) {
//    DiaryGoodVO diaryGoodVO = this.diaryGoodProc.read(goodno);
//    model.addAttribute(diaryGoodVO);
//    return "/diarygood/read";
//  }
  
  
//  @GetMapping(value="/read")
//  public String read()
  
  @GetMapping(value="/list_all")
  public String list_all(Model model) {
    ArrayList<DiaryGoodVO> list = this.diaryGoodProc.list_all();
    model.addAttribute("list", list);
    return "/diarygood/list_all";
  }
  
  @PostMapping(value = "/delete")
  public String delete_proc(HttpSession session, 
      Model model, 
      @RequestParam(name="goodno", defaultValue = "0") int goodno, 
      RedirectAttributes ra) {    
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      this.diaryGoodProc.delete(goodno);

      return "redirect:/diarygood/list_all";

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/diarygood/post2get"; // @GetMapping(value = "/msg")
    }
  }
  
}
