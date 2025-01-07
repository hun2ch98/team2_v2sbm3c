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
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/bannedwordsgood")
public class BannedwordsgoodCont {
  @Autowired
  @Qualifier("dev.mvc.bannedwordsgood.BannedwordsgoodProc")
  BannedwordsgoodProcInter bannedwordsgoodProc;
	
  public BannedwordsgoodCont() {
	System.out.println("-> BannedwordsgoodCont created.");
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
  
	
}
