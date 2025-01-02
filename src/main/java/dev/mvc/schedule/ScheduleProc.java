//package dev.mvc.schedule;
//
//import java.util.ArrayList;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component("dev.mvc.schedule.ScheduleProc")
//public class ScheduleProc implements ScheduleProcInter {
//
//  @Autowired 
//  private ScheduleDAOInter scheduleDAO;
//  
//  
//  @Override
//  public int create(ScheduleVO scheduleVO) {
//    int cnt = this.scheduleDAO.create(scheduleVO);
//    return cnt;
//  }
//
//  
//  @Override
//  public ArrayList<ScheduleVO> list_all() {
//    ArrayList<ScheduleVO> list = this.scheduleDAO.list_all();
//    return list;
//  }
//
//  
//  @Override
//  public ScheduleVO read(int scheduleno) {
//    ScheduleVO scheduleVO = this.scheduleDAO.read(scheduleno);
//    return scheduleVO;
//  }
//
//
//  @Override
//  public int update(ScheduleVO scheduleVO) {
//    int cnt = this.scheduleDAO.update(scheduleVO);
//    return cnt;
//  }
//
//  
//  @Override
//  public int delete(int scheduleno) {
//    int cnt = this.scheduleDAO.delete(scheduleno);
//    return cnt;
//  }
//  
//  
//  
//  
//  
//  
//  
//}
