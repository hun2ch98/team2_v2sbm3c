package dev.mvc.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class CalendarCont {

    @Autowired
    private CalendarProcInter calendarProc;

    @GetMapping("/calendar/data")
    public List<Map<String, Object>> getCalendarData() {
        // SQL 결과를 JSON으로 변환
        return calendarProc.list().stream()
                .map(calendar -> {
                    // 명시적으로 Map 생성
                    Map<String, Object> map = Map.of(
                        "date", calendar.getLabel_date(),
                        "illustration", calendar.getIllustno(),
                        "weather", calendar.getWeatherno(),
                        "emotion", calendar.getEmno()
                    );
                    return map;
                })
                .collect(Collectors.toList());
    }
}
