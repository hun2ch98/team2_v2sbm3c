package dev.mvc.calendar;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.diary.DiaryVO;
import dev.mvc.member.MemberProcInter;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/calendar")
public class CalendarCont {
	
	@Autowired
	@Qualifier("dev.mvc.member.MemberProc") 
	private MemberProcInter memberProc;
	
	@Autowired
  @Qualifier("dev.mvc.calendar.CalendarProc") 
  private CalendarProcInter calendarProc;
	
	
	// http://localhost:9093/diary/create
	/**
	 * create form
	 * @param model
	 * @param date
	 * @param diaryno
	 * @return
	 */
	@GetMapping(value="/create")
	public String create(Model model) {
		CalendarVO calendarVO = new CalendarVO();
		DiaryVO diaryVO = new DiaryVO();
		
		model.addAttribute(calendarVO);
		model.addAttribute(diaryVO);
		
		return "/calendar/create";
	}

	@PostMapping(value="/create")
	public String create(Model model, HttpSession session, 
                            			BindingResult bindingResult, RedirectAttributes ra,
                            			@RequestParam(name="date", defaultValue="") String date, 
                                  @RequestParam(name="diaryno", defaultValue="1") int diaryno, 
                            			@ModelAttribute("calendarVO") CalendarVO calendarVO, 
                            			@ModelAttribute("diaryVO") DiaryVO diaryVO) {
		
		if (this.memberProc.isMemberAdmin(session)) {
			if (bindingResult.hasErrors()) {
				return "/diary/create";
			}
			
			calendarVO.setLabel_date(date);
	    calendarVO.setDiaryno(diaryno);
			
	    return "redirect:/diary/list_by_illustno_search_paging";
		} else {
			return "redirect:/member/login_cookie_need";
		}
		
	}
	
	/**
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping(value="/list_all")
	public String list_all(HttpSession session, Model model) {
	  
	  if (this.memberProc.isMemberAdmin(session)) {
	    ArrayList<CalendarVO> list = this.calendarProc.list_all();
	    model.addAttribute("list", list);
	    return "redirect:/calendar/list_calendar";
    } else {
      return "redirect:/member/login_cookie_need";	  }
	 
	}
	
	/**
	 * 
	 * @param model
	 * @param calendarno
	 * @param date
	 * @param now_page
	 * @return
	 */
	@GetMapping(path="/read")
	public String read(Model model, 
	                              @PathVariable("calendarno") int calendarno,
	                              @RequestParam(name="date", defaultValue="") String date,
	                              @RequestParam(name="now_page", defaultValue = "1") int now_page) {
	  CalendarVO calendarVO = this.calendarProc.read(calendarno);
	  model.addAttribute("calendarVO", calendarVO);
	  model.addAttribute("date", date);
	  model.addAttribute("now_page", now_page);
	  
	  return "/calendar/read";
	}
	
	
	
}
