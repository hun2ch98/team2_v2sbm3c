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
import dev.mvc.tool.MailTool;

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
  public String form(Model model) {
    return "/key/form";
  }
  
  
  @GetMapping(value = "/isExist")
  @ResponseBody
  public String isExist(@RequestParam(name = "name", defaultValue = "") String name,
                        @RequestParam(name = "recovery_key", defaultValue = "") String recovery_key) {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("name", name);
    map.put("recovery_key", recovery_key);
    
    System.out.println("-> name: " + name);
    System.out.println("-> recovery_key: " + recovery_key);
    
    String recoveryKey = this.memberProc.find_pw_check(map);
    
    JSONObject obj = new JSONObject();
    obj.put("recovery_key", recoveryKey);
    
    return obj.toString();
  }
  
  
  @PostMapping(value = "/send")
  public String send(@RequestParam(name = "email", defaultValue = "") String email,
      @RequestParam(name = "name", defaultValue = "") String name,
      @RequestParam(name = "recovery_key", defaultValue = "") String recovery_key) {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("name", name);
    map.put("recovery_key", recovery_key);
    
    String recoveryKey = this.memberProc.find_pw_check(map);
    System.out.println("-> recovery_key : " + recoveryKey);
    
    // 비밀번호를 가져오는 로직
    String password = this.memberProc.find_pw_check(map); // 실제 비밀번호를 가져옵니다.
    System.out.println("-> password : " + password);
    
    String from = "skt3246@gmail.com";
    String title = "[Hoak]" + name + " 회원님의 비밀번호입니다.";
    String content = "[Hoak]" + name + "회원님의 비밀번호는 " + password + "입니다.";
    MailTool mailTool = new MailTool();
    mailTool.send(email, from, title, content); // 메일 전송
    
    return "/key/sended";
  }
}
