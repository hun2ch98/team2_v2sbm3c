package dev.mvc.noticegood;

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
@RequestMapping(value = "/noticegood")
public class NoticegoodCont {
  
  @Autowired
  @Qualifier("dev.mvc.noticegood.NoticegoodProc")
  NoticegoodProcInter noticegoodProc;
  
  public NoticegoodCont() {
    System.out.println("-> NoticegoodCont created.");
  }
  
  /**
   * 등록
   * @param session
   * @param noticegoodVO
   * @return
   */
  @PostMapping(value = "/create")
  @ResponseBody
  public String create(HttpSession session, @RequestBody NoticegoodVO noticegoodVO) {
    System.out.println("-> 수신 데이터: " + noticegoodVO.toString()); // json_src: {"current_passwd":"1234"}
    
//    int memberno = (int)session.getAttribute("memberno"); // 보안성 향상
    int memberno = 1; // 테스트 용
    noticegoodVO.setMemberno(memberno);
    
    int cnt = this.noticegoodProc.create(noticegoodVO);
    
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
  // http://localhost:9093/noticegood/list_all
  @GetMapping(value = "/list_all")
  public String list_all(Model model) {
    ArrayList<NoticegoodVO> list = this.noticegoodProc.list_all();
    model.addAttribute("list", list);

    return "/noticegood/list_all"; // /templates/noticegood/list_all.html
  }
}
