//package dev.mvc.calendar;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//
//import dev.mvc.diary.DiaryVO;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service("dev.mvc.calendar.CalendarProc")
//public class CalendarProc implements CalendarProcInter {
//	
//	@Autowired
//	CalendarDAOInter calendarDAO;
//
//	@Override
//	public int create(CalendarVO calendarVO) {
//		int cnt = this.calendarDAO.create(calendarVO);
//		return cnt;
//	}
//
//  @Override
//  public ArrayList<CalendarVO> list_all() {
//    ArrayList<CalendarVO> list = this.calendarDAO.list_all();
//    return list;
//  }
//
//  @Override
//  public CalendarVO read(int calendarno) {
//   CalendarVO calendarVO = this.calendarDAO.read(calendarno);
//    return calendarVO;
//  }
//   
//  
//
//}
