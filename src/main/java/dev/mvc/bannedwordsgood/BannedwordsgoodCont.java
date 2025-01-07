package dev.mvc.bannedwordsgood;

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
@RequestMapping(value = "/bannedwordsgood")
public class BannedwordsgoodCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
   private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.bannedwordsgood.BannedwordsgoodProc")
  BannedwordsgoodProcInter bannedwordsgoodProc;
	
  public BannedwordsgoodCont() {
	System.out.println("-> BannedwordsgoodCont created.");
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
//    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }
  
  @PostMapping(value= "/create")
  @ResponseBody
  public String create(HttpSession session, @RequestBody BannedwordsgoodVO bannedwordsgoodVO) { 
	System.out.println("-> 수신 데이터:" + bannedwordsgoodVO.toString());
	   
	  int memberno =1;
//	  int memberno = (int)session.getAttribute("memberno"); // 보안성 향상
	  bannedwordsgoodVO.setMemberno(memberno);
	    
	  int cnt = this.bannedwordsgoodProc.create(bannedwordsgoodVO);
	  JSONObject json = new JSONObject();
	  json.put("res", cnt);
	    
	  return json.toString();
	}
	
  /**
   * 목록
   * 
   * @param model
   * @return
   */
  // http://localhost:9091/cate/list_all
  @GetMapping(value = "/list_all")
  public String list_all(Model model) {
    ArrayList<BannedwordsgoodVO> list = this.bannedwordsgoodProc.list_all();
    model.addAttribute("list", list);

//	    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//	    model.addAttribute("menu", menu);

    return "/bannedwordsgood/list_all"; // /templates/bannedwordsgood/list_all.html
  }
  
  /**
   * 삭제 처리
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete_proc(HttpSession session, 
      Model model, 
      @RequestParam(name="goodno", defaultValue = "0") int goodno, 
      RedirectAttributes ra) {    
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      this.bannedwordsgoodProc.delete(goodno);

      return "redirect:/bannedwordsgood/list_all";

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/bannedwordsgood/post2get"; // @GetMapping(value = "/msg")
    }

  }
  
	
}
