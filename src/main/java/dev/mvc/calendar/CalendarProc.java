package dev.mvc.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import dev.mvc.diary.DiaryVO;

import java.util.List;

@Service
public class CalendarProc implements CalendarProcInter {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<CalendarVO> list() {
        String sql = """
            SELECT c.calendarno, c.labeldate AS label_date, c.diaryno,
                   d.diaryno AS diaryno, d.title AS title, d.ddate AS ddate,
                   d.summary AS summary, d.weatherno AS weatherno, d.emno AS emno,
                   d.illustno AS illustno
            FROM calendar c
            JOIN diary d ON c.diaryno = d.diaryno
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CalendarVO calendarVO = new CalendarVO();
            calendarVO.setCalendarno(rs.getInt("calendarno"));
            calendarVO.setLabel_date(rs.getString("label_date"));
            calendarVO.setDiaryno(rs.getInt("diaryno"));

            // DiaryVO를 매핑
            DiaryVO diaryVO = new DiaryVO();
            diaryVO.setDiaryno(rs.getInt("diaryno"));
            diaryVO.setTitle(rs.getString("title"));
            diaryVO.setDdate(rs.getDate("ddate"));
            diaryVO.setSummary(rs.getString("summary"));
            diaryVO.setWeatherno(rs.getInt("weatherno"));
            diaryVO.setEmno(rs.getInt("emno"));
            diaryVO.setIllustno(rs.getInt("illustno"));

            calendarVO.setDiaryVO(diaryVO); // CalendarVO에 DiaryVO 설정
            return calendarVO;
        });
    }


}
