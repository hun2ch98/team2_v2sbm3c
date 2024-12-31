package dev.mvc.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.mvc.diary.DiaryVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// JSON 데이터를 반환하는 컨트롤러
@RestController
public class CalendarDataCont {

    @Autowired
    private CalendarProcInter calendarProc;

    @GetMapping("/calendar/data")
    public List<Map<String, Object>> getCalendarData() {
        return calendarProc.list().stream()
            .map(calendar -> {
                DiaryVO diary = calendar.getDiaryVO();
                Map<String, Object> map = new HashMap<>();
                map.put("date", calendar.getLabel_date());
                map.put("illustration", diary.getIllustno());
                map.put("weather", diary.getWeatherno());
                map.put("emotion", diary.getEmno());
                return map;
            })
            .collect(Collectors.toList());
    }
}


