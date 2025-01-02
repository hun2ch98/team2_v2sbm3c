package dev.mvc.calendar;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	
	
	// http://localhost:9093/calendar/create
	/**
	 * create form
	 * @param model
	 * @param date
	 * @param diaryno
	 * @return
	 */
	@GetMapping(value="/create")
	public String create(Model model, 
			@RequestParam(name="date", defaultValue="") String date, 
			@RequestParam(name="diaryno", defaultValue="1") int diaryno) {
		CalendarVO calendarVO = new CalendarVO();
		DiaryVO diaryVO = new DiaryVO();
		
		model.addAttribute(calendarVO);
		model.addAttribute(diaryVO);
		
		calendarVO.setLabel_date(date);
		calendarVO.setDiaryno(diaryno);
		return "/calendar/create";
	}

	@PostMapping(value="/create")
	public String create(Model model, HttpSession session, 
			BindingResult bindingResult, RedirectAttributes ra,
			@ModelAttribute("calendarVO") CalendarVO calendarVO) {
		
		if (this.memberProc.isMemberAdmin(session)) {
			if (bindingResult.hasErrors()) {
				return "/calendar/create";
			}
			
			
		} else {
			return "redirect:/member/login_cookie_need";
		}
		
		
		return "";
	}
	
}
