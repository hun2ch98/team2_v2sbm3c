package dev.mvc.schedule;

import java.time.LocalDate;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.schedule.ScheduleVO;
import dev.mvc.member.MemberProcInter;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping(value="/schedule")
public class ScheduleCont {

  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Component("dev.mvc.schedule.ScheduleProc")
  private MemberProcInter memberProc; 
  
  @Autowired
  @Qualifier("dev.mvc.schedule.ScheduleProc") // @Component("dev.mvc.schedule.ScheduleProc")
  private ScheduleProcInter scheduleProc; 
  
  public ScheduleCont() {
    System.out.println("-> ScheduleCont created.");
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
  
  // http://localhost:9091/schedule/create
  @GetMapping(value = "/create")
  public String create(Model model) {
    ScheduleVO scheduleVO = new ScheduleVO();
    model.addAttribute("scheduleVO", scheduleVO);
    return "/schedule/create"; // /templates/schedule/create.html

  }
  
  /**
   * 등록 처리, http://localhost:9091/schedule/create
   * 
   * @return
   */
  @PostMapping(value = "/create")
  public String create(HttpSession session, Model model, 
           @ModelAttribute("scheduleVO") ScheduleVO scheduleVO) {
    
    int memberno = (int) session.getAttribute("memberno");
    scheduleVO.setMemberno(memberno);
    int cnt = this.scheduleProc.create(scheduleVO);

    if (cnt == 1) {
      // model.addAttribute("code", "create_success");
      // model.addAttribute("name", cateVO.getName());

      return "redirect:/schedule/list_all"; // @GetMapping(value="/list_all")
    } else {
      model.addAttribute("code", "create_fail");
    }

    model.addAttribute("cnt", cnt);

    return "/schedule/msg"; // /templates/schedule/msg.html
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
    ArrayList<ScheduleVO> list = this.scheduleProc.list_all();
    model.addAttribute("list", list);

    return "/schedule/list_all"; // /templates/schedule/list_all.html
  }
 
  /**
   * 조회 http://localhost:9091/schedule/read/1
   * 
   * @return
   */
  @GetMapping(path = "/read/{scheduleno}")
  public String read(Model model, @PathVariable("scheduleno") int scheduleno) {
   
    ScheduleVO scheduleVO = this.scheduleProc.read(scheduleno);

    model.addAttribute("scheduleVO", scheduleVO);

    return "/schedule/read";
  }
  
  /**
   * 수정 폼 http:// localhost:9091/schedule/update?scheduleno=1
   *
   */
  @GetMapping(value = "/update/{scheduleno}")
  public String update(HttpSession session, Model model, 
      @PathVariable("scheduleno") int scheduleno, 
      RedirectAttributes ra) {    

    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      ScheduleVO scheduleVO = this.scheduleProc.read(scheduleno);
      model.addAttribute("scheduleVO", scheduleVO);

      return "/schedule/update"; // /templates/schedule/update.html
    } else {
      return "/member/login_cookie_need"; // /templates/member/login_cookie_need.html
    }

  }
  
  /**
   * 수정 처리 http://localhost:9091/schedule/update?scheduleno=1
   * 
   * @return
   */
  @PostMapping(value = "/update")
  public String update(HttpSession session, Model model, RedirectAttributes ra,
      @Valid @ModelAttribute("scheduleVO") ScheduleVO scheduleVO) {
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      int cnt = this.scheduleProc.update(scheduleVO); // 글수정
      
      if (cnt == 1) {
        return "redirect:/schedule/list_all";
      } else {
        model.addAttribute("code", "update_fail");
      }
      model.addAttribute(cnt);
      return "/schedule/msg";
    } else {
      return  "/member/login_cookie_need";
    }

  }
  
  /**
   * 삭제폼 http://localhost:9091/schedule/delete/1
   */
  @GetMapping(path = "/delete/{scheduleno}")
  public String delete(HttpSession session, Model model, 
      RedirectAttributes ra, @PathVariable("scheduleno") Integer scheduleno) {
    if (this.memberProc.isMemberAdmin(session)) {
      ScheduleVO scheduleVO = this.scheduleProc.read(scheduleno);
      model.addAttribute("scheduleVO", scheduleVO);

      return "/schedule/delete"; // templaes/cate/delete.html
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
  }
  
  /**
   * 삭제 처리, http://localhost:9091/cate/delete?cateno=1
   * 
   * @param model         Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @param cateVO        Form 태그 값 -> 검증 -> cateVO 자동 저장, request.getParameter()
   *                      자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete_process(HttpSession session, Model model,  RedirectAttributes ra, 
      @RequestParam(name = "scheduleno", defaultValue = "0") Integer scheduleno ) {
    if (this.memberProc.isMemberAdmin(session)) {
      this.scheduleProc.delete(scheduleno);
      return "redirect:/schedule/list_all";
     
    } else {
      return "redirect:/schedule/post2get";  // redirect
    }
  }
  
  
  /**
   * 특정 날짜의 목록
   * @param session
   * @param model
   * @param date
   * @return
   */
  @GetMapping(value="/list_calendar")
  public String list_calendar(HttpSession session, Model model, 
      @RequestParam(name="year", defaultValue="0") int year, 
	  @RequestParam(name="month", defaultValue="0") int month) {

      if (year == 0) {
    	  // 현재 날짜를 가져옴
    	  LocalDate today  = LocalDate.now();
    	  
    	  //연도와 월을 추출
    	  year = today.getYear();
    	  month = today.getMonthValue();
      }
      
      String month_str = String.format("%02d", month); // 두 자리 형식으로
	  System.out.println("-> month: " + month_str);
	
	  String date = year + "-" + month;
	  System.out.println("-> date: " + date);
	  
	  model.addAttribute("year", year);
	  model.addAttribute("month", month-1);  // javascript는 1월이 0임. 
	    
	  return "/schedule/list_calendar"; // /templates/calendar/list_calendar.html
  }
  
  /**
   * 특정 날짜의 목록
   * 
   * @param model
   * @return
   */
  // http://localhost:9091/calendar/list_calendar_day?labeldate=2025-01-03
  @GetMapping(value = "/list_calendar_day")
  @ResponseBody
  public String list_calendar_day(Model model, @RequestParam(name="sdate", defaultValue = "") String sdate) {
	System.out.println("-> sdate: " + sdate);
	
    ArrayList<ScheduleVO> list = this.scheduleProc.list_calendar_day(sdate);
    model.addAttribute("list", list);

    JSONArray schedule_list = new JSONArray();
    
    for (ScheduleVO scheduleVO: list) {
        JSONObject schedual = new JSONObject();
        schedual.put("scheduleno", scheduleVO.getScheduleno());
        schedual.put("sdate", scheduleVO.getSdate());
        schedual.put("label", scheduleVO.getLabel());
        schedual.put("seqno", scheduleVO.getSeqno());
        
        schedule_list.put(schedual);
    }

    return schedule_list.toString();
    
  }
  
  
}