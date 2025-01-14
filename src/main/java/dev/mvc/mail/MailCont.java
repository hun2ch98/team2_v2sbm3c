package dev.mvc.mail;

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
@RequestMapping("/mail")
public class MailCont {
  public MailCont() {
    System.out.println("-> MailCont created.");
  }
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  /**
   * 메일 입력 화면 -> http://localhost:9093/mail/form
   * @param model
   * @return
   */
  @GetMapping(value = "/form")
  public String form(Model model) {
    return "/mail/form";
  }
  
  /**
   * 아이디 찾이에거 가입 유무 확인 db 검증
   * @param name
   * @param phone
   * @return
   */
  @GetMapping(value="/isExist") // http://localhost:9093/mail/isExist?name=name&phone=phone
  @ResponseBody
  public String isExist(@RequestParam(name="name", defaultValue = "")String name,
                        @RequestParam(name="phone", defaultValue = "")String phone) {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("name", name);
    map.put("phone", phone);
    
    System.out.println("-> name: " + name);
    System.out.println("-> phone: " + phone);
    
    String id = this.memberProc.find_id_check(map);
    
    System.out.println("-> id: " + id);
    
    JSONObject obj = new JSONObject();
    obj.put("id", id);
    
    return obj.toString();
  }
  
  /**
   * 메일 전송
   * @param email
   * @param name
   * @param phone
   * @return
   */
  @PostMapping(value = "/send")
  public String send(@RequestParam(name = "email", defaultValue = "") String email,
                     @RequestParam(name = "name", defaultValue = "") String name,
                     @RequestParam(name = "phone", defaultValue = "") String phone) {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("name", name);
    map.put("phone", phone);
    
    String id = this.memberProc.find_id_check(map);
    System.out.println("-> id :" + id);
    
    String from = "skt3246@gmail.com";
    String title = "[Hoak]" + name + " 회원님의 아이디입니다.";
    String content = "[Hoak]" + name + "회원님의 아이디는 " + id + "입니다.";
    MailTool mailTool = new MailTool();
    mailTool.send(email, from, title, content); // 메일 전송
    
    return "/mail/sended";
  }
}
