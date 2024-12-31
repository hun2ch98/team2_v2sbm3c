package dev.mvc.calendar;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// HTML 템플릿을 반환하는 컨트롤러
@Controller
public class CalendarViewCont {

    @GetMapping("/calendar/view")
    public String viewCalendar() {
        return "calendar/calendar"; // templates/calendar/calendar.html 경로를 반환
    }
}