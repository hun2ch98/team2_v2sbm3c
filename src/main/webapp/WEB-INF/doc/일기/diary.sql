DROP TABLE diary;
CREATE TABLE diary (
    diaryno NUMBER(30) PRIMARY KEY,
    title VARCHAR2(50) NULL,
    ddate DATE               NULL,
    summary CLOB          ,
    weather_code NUMBER(10) NULL,
    emono NUMBER(20)       NULL,
    memberno NUMBER(30)      NULL,
    illustno NUMBER(10) NULL

);

commit;

DROP SEQUENCE diary_seq;

CREATE SEQUENCE diary_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999 --> NUMBER(7) 대응
  CACHE 2                       -- 2번은 메모리에서만 계산
  NOCYCLE;                     -- 다시 1부터 생성되는 것을 방지
  
  
SELECT table_name FROM user_tables WHERE table_name = 'DIARY';

