package dev.mvc.log;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.member.MemberProcInter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/log")
public class LogCont {
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;

  /** 페이징 목록 주소 */
  private String list_file_name = "/log/list_all";
  
  @Autowired
  @Qualifier("dev.mvc.log.LogProc")
  private LogProcInter logProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") 
  private MemberProcInter memberProc;
  
  public LogCont() {
    System.out.println("LogCont created.");
  }

  @RequestMapping(value="/list_all")
  public String list_all(Model model, HttpSession session,
      @RequestParam(name="logno", defaultValue="1") int logno, 
      @RequestParam(value = "table_name", required = false, defaultValue = "") String table_name,
      @RequestParam(value = "action", required = false, defaultValue = "") String action,
      @RequestParam(value = "ip", required = false, defaultValue = "") String ip, 
      @RequestParam(value = "is_success", required = false, defaultValue = "") String is_success, 
      @RequestParam(value = "start_date", required = false, defaultValue = "") String start_date,
      @RequestParam(value = "end_date", required = false, defaultValue = "") String end_date,
      @RequestParam(value = "now_page", required = false, defaultValue = "1") int now_page) {
    
    int record_per_page = 10; 

    table_name = table_name.trim();
    action = action.trim();
    ip = ip.trim();
    start_date = start_date.trim();
    end_date = end_date.trim();
    
    ArrayList<LogVO> list = logProc.list_all();
    model.addAttribute(list);
    
    int start_num = (now_page - 1) * record_per_page + 1;
    int end_num = now_page * record_per_page;
    
    int search_count = logProc.countSearchResults(table_name, action, ip, is_success, start_date, end_date);
    ArrayList<LogVO> logList = logProc.list_search_paging(table_name, action, ip, is_success, now_page, record_per_page, start_num, end_num);
    

    String paging = logProc.pagingBox(now_page, table_name, action, ip, is_success, start_date, end_date, list_file_name, search_count, record_per_page, page_per_block);
    
    model.addAttribute("logno", logno);
    model.addAttribute("logList", logList);
    model.addAttribute("table_name", table_name);
    model.addAttribute("action", action);
    model.addAttribute("ip", ip);
    model.addAttribute("is_success", is_success);
    model.addAttribute("start_date", start_date);
    model.addAttribute("end_date", end_date);
    model.addAttribute("search_count", search_count);
    model.addAttribute("now_page", now_page);
    model.addAttribute("paging", paging);
   
    
    return "/log/list_all";
  }
  
  @GetMapping(path="/delete/{logno}")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,
      @PathVariable("logno") int logno, 
      @RequestParam(value = "table_name", required = false, defaultValue = "") String table_name,
      @RequestParam(value = "action", required = false, defaultValue = "") String action,
      @RequestParam(value = "ip", required = false, defaultValue = "") String ip, 
      @RequestParam(value = "is_success", required = false, defaultValue = "") String is_success, 
      @RequestParam(value = "start_date", required = false, defaultValue = "") String start_date,
      @RequestParam(value = "end_date", required = false, defaultValue = "") String end_date,
      @RequestParam(value = "now_page", required = false, defaultValue = "1") int now_page) {

    if (this.memberProc.isMemberAdmin(session)) {
      model.addAttribute("logno", logno);
      model.addAttribute("table_name", table_name);
      model.addAttribute("action", action);
      model.addAttribute("ip", ip);
      model.addAttribute("is_success", is_success);
      model.addAttribute("start_date", start_date);
      model.addAttribute("end_date", end_date);
      model.addAttribute("now_page", now_page);
      
      LogVO logVO_delete = this.logProc.read(logno);
      model.addAttribute("logVO_delete", logVO_delete);
      return "/log/delete";
    } else {
      return "/member/login_cookie_need"; 
    }
  }
  
  
  @PostMapping(value = "/delete")
  public String delete(HttpSession session, Model model, RedirectAttributes ra, HttpServletRequest request,
      @RequestParam(name="logno", defaultValue="1") int logno, 
      @RequestParam(value = "table_name", required = false, defaultValue = "") String table_name,
      @RequestParam(value = "action", required = false, defaultValue = "") String action,
      @RequestParam(value = "ip", required = false, defaultValue = "") String ip, 
      @RequestParam(value = "is_success", required = false, defaultValue = "") String is_success, 
      @RequestParam(value = "start_date", required = false, defaultValue = "") String start_date,
      @RequestParam(value = "end_date", required = false, defaultValue = "") String end_date,
      @RequestParam(value = "now_page", required = false, defaultValue = "1") int now_page) {
    
    if (this.memberProc.isMemberAdmin(session)) {
      model.addAttribute("logno", logno);
      model.addAttribute("table_name", table_name);
      model.addAttribute("action", action);
      model.addAttribute("ip", ip);
      model.addAttribute("is_success", is_success);
      model.addAttribute("start_date", start_date);
      model.addAttribute("end_date", end_date);
      model.addAttribute("now_page", now_page);
      
      LogVO logVO_delete= this.logProc.read(logno);
      model.addAttribute("logVO_delete", logVO_delete);
      
      this.logProc.delete(logno);
      return "redirect:/log/list_all";
    } else {
      return "/member/login_cookie_need"; 
    }
  }
  
  
}
