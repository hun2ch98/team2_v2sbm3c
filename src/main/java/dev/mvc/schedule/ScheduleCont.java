//package dev.mvc.schedule;
//
//import java.util.ArrayList;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import dev.mvc.member.MemberProcInter;
//import jakarta.servlet.http.HttpSession;
//
//@Controller
//@RequestMapping(value="/schedule")
//public class ScheduleCont {
//
//  @Autowired
//  @Qualifier("dev.mvc.member.MemberProc") // @Component("dev.mvc.calendar.CalendarProc")
//  private MemberProcInter memberProc; 
//  
//  @Autowired
//  @Qualifier("dev.mvc.schedule.ScheduleProc") // @Component("dev.mvc.calendar.CalendarProc")
//  private ScheduleProcInter scheduleProc; 
//  
//  public ScheduleCont() {
//    System.out.println("-> ScheduleCont created.");
//  }
//  
//  /**
//   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
//   * 전송
//   * 
//   * @return
//   */
//  @GetMapping(value = "/post2get")
//  public String post2get(Model model, 
//      @RequestParam(name="url", defaultValue = "") String url) {
//    return url; // forward, /templates/...
//  }
//  
//  /**
//   * 등록 처리, http://localhost:9091/calendar/create
//   * 
//   * @return
//   */
//  @PostMapping(value = "/create")
//  public String create(HttpSession session, Model model, 
//           @ModelAttribute("scheduleVO") ScheduleVO scheduleVO) {
//    
//    // int memberno = (int)session.getAttribute("memberno");
//    int memberno = 1; // 테스트용
//    scheduleVO.setMemberno(memberno);
//    
//    int cnt = this.scheduleProc.create(scheduleVO);
//
//    if (cnt == 1) {
//      // model.addAttribute("code", "create_success");
//      // model.addAttribute("name", cateVO.getName());
//
//      return "redirect:/schedule/list_all"; // @GetMapping(value="/list_all")
//    } else {
//      model.addAttribute("code", "create_fail");
//    }
//
//    model.addAttribute("cnt", cnt);
//
//    return "/calendar/msg"; // /templates/calendar/msg.html
//  }
//  
//  
//  
//  
//  
//  
//  
//  
//  
//  
//}
