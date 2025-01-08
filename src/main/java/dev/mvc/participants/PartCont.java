package dev.mvc.participants;

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
import dev.mvc.surveygood.SurveygoodProcInter;
import dev.mvc.surveygood.SurveygoodVO;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/participants")
public class PartCont {
  
  @Autowired
  @Qualifier("dev.mvc.participants.PartProc")
  PartProcInter partProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  public PartCont() {
    System.out.println("-> PartCont created.");
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

    return url; 
  }
  
  /**
   * 등록
   * @param session
   * @param surveygoodVO
   * @return
   */
  @PostMapping(value="/create")
  @ResponseBody
  public String create(HttpSession session, @RequestBody PartVO partVO) {
    System.out.println("-> 수신 데이터: " + partVO.toString());
    
    int memberno = 2;
//    int memberno = (int)session.getAttribute("memberno");
    partVO.setMemberno(memberno);
    
    int cnt = this.partProc.create(partVO);
    
    JSONObject json = new JSONObject();
    json.put("res", cnt);
    
    return json.toString();
  }
  
  /**
   * 목록
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(Model model) {
    ArrayList<PartVO> list = this.partProc.list_all();
    model.addAttribute("list", list);

    return "/participants/list_all"; 
  }
  
  /**
   * 삭제 처리 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete_proc(HttpSession session, 
      Model model, 
      @RequestParam(name="pno", defaultValue = "0") int pno, 
      RedirectAttributes ra) {    
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      this.partProc.delete(pno);   // 삭제

      return "redirect:/participants/list_all";

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/participants/post2get"; // @GetMapping(value = "/msg")
    }

  }


}
