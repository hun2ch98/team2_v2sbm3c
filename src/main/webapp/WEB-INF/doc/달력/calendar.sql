DROP TABLE calendar;

CREATE TABLE calendar (
  calendarno  NUMBER(30) NOT NULL PRIMARY KEY, 
  labeldate   VARCHAR2(10)  NOT NULL, -- 출력할 날짜 2013-10-20
  diaryno     NUMBER(30)    NOT NULL, -- 일기 고유 번호
  FOREIGN KEY (diaryno) REFERENCES Diary(diaryno)
);

DROP SEQUENCE CALENDAR_SEQ;
CREATE SEQUENCE calendar_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999 --> NUMBER(7) 대응
  CACHE 2                       -- 2번은 메모리에서만 계산
  NOCYCLE; 