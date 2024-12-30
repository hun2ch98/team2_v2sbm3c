package dev.mvc.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarProc implements CalendarProcInter {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<CalendarVO> list() {
        // 캘린더와 일기 테이블을 JOIN하여 필요한 데이터 가져오기
        String sql = """
                SELECT c.calendarno, c.labeldate AS label_date, c.diaryno,
                       d.illustno, d.weatherno, d.emno
                FROM calendar c
                JOIN diary d ON c.diaryno = d.diaryno
                """;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CalendarVO.class));
    }
}
