package dev.mvc.calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE calendar (
//    calendarno  NUMBER(30) NOT NULL PRIMARY KEY, 
//    label_date   VARCHAR2(10)  NOT NULL, -- 출력할 날짜 2013-10-20
//    diaryno     NUMBER(30)    NOT NULL, -- 일기 고유 번호
//    FOREIGN KEY (diaryno) REFERENCES Diary(diaryno)
//  );

@Getter @Setter @ToString
public class CalendarVO {
  
  private int Calendarno;
  
  /** 출력할 날짜 */
  private String label_date="";
  
  /** 일기 고유 번호 */
  private int diaryno;

}
