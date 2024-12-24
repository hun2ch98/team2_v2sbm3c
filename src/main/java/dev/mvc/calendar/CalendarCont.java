package dev.mvc.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/calendar")
public class CalendarCont {

    @Autowired
    private CalendarProcInter calendarProc;

    // 월간 캘린더 조회
    @GetMapping("/list")
    public String list(Model model) {
        List<CalendarVO> list = calendarProc.list();
        model.addAttribute("calendarList", list);
        return "calendar/list"; // Thymeleaf 파일 경로
    }
}
