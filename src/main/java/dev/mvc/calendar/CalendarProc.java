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
        String sql = "SELECT calendarno, label_date, diaryno FROM calendar";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CalendarVO.class));
    }
}
