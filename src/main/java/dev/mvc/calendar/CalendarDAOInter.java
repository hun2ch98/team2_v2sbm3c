package dev.mvc.calendar;

import java.util.ArrayList;
import java.util.List;

public interface CalendarDAOInter {
    
	/**
	 * 일정 등록
	 * @param calendarVO
	 * @return
	 */
	public int create(CalendarVO calendarVO);
	
	/**
	 * 일정 목록
	 * @return
	 */
	public ArrayList<CalendarVO> list_all();

	/**
	 * 조회
	 * @param calendarno
	 * @return
	 */
  public CalendarVO read(int calendarno);
	
}
