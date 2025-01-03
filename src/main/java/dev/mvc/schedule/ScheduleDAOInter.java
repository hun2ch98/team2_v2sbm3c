package dev.mvc.schedule;

import java.util.ArrayList;


public interface ScheduleDAOInter {

  int create(ScheduleVO scheduleVO);
  
  
  public ArrayList<ScheduleVO> list_all();
  
  
  public ScheduleVO read(int scheduleno);
  
  
  public int update(ScheduleVO scheduleVO);

  
  public int delete(int scheduleno);
  
  public ArrayList<ScheduleVO> list_calendar(String date);
  
  public ArrayList<ScheduleVO> list_calendar_day(String date);
  
  
  
  
  
}
