package dev.mvc.key;

import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dev.mvc.member.MemberProcInter;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/key")
public class KeyCont {
  public KeyCont() {
    System.out.println("-> KeyCont create.");
  }
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  /**
   * 복구키 입력 화면 -> http://localhost:9093/key/form
   * @param model
   * @return
   */
  @GetMapping(value = "/form")
  public String form(Model model, HttpSession session) {
    
    // ID는 세션에 저장, 이후 복구키 검증 시 사용
    session.setAttribute("id", "user1");
    
    return "/key/form"; // 복구키 입력 화면
  }
  
  /**
   * 비밀번호 찾기 -> 복구키 유무 확인
   * @param name
   * @param recovery_key
   * @return
   */
  @GetMapping(value = "/isExist")
  @ResponseBody
  public String isExist(HttpSession session,
      @RequestParam(name = "id", defaultValue = "") String id,
      @RequestParam(name = "recovery_key", defaultValue = "") String recovery_key) {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("id", id);
    map.put("recovery_key", recovery_key);
    
    System.out.println("-> id: " + id);
    System.out.println("-> recovery_key: " + recovery_key);
    
    int cnt = this.memberProc.find_pw_check(map); // 복구키 검증
    JSONObject obj = new JSONObject();
    
    if(cnt == 1) { // 복구키가 일치하는 경우
      session.setAttribute("authenticatedID", id); // 인증된 ID 저장
    }
    
    obj.put("cnt", cnt);
    return obj.toString();
  }
  
  /** 복구키 인증 완료된 후 비밀번호 변경 폼 */
  @GetMapping(value = "/update_passwd")
  public String update_passwd_form(Model model) {
    return "/key/update_passwd"; // 비밀번호 변경 폼
  }
  
  /**
   * 복구키 인증 완료된 후 비밀번호 변경 처리
   * @param model
   * @param session
   * @param passwd
   * @return
   */
  @PostMapping(value = "/update_passwd")
  @ResponseBody
  public String update_passwd_proc(Model model, HttpSession session,
      @RequestParam(name = "passwd", defaultValue = "") String passwd) {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("id", (String)session.getAttribute("authenticatedID"));
    map.put("passwd", passwd);
    
    JSONObject obj = new JSONObject();
    
    int cnt = this.memberProc.update_passwd(map);
    obj.put("cnt", cnt);
    
    return obj.toString();
  }
}
