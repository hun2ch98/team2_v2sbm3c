DROP TABLE WEATHER;
CREATE TABLE weather (
    weatherno   NUMBER(10)    NOT NULL    PRIMARY KEY,
    w_type      VARCHAR(100)    NOT NULL,
    w_img       VARCHAR(100)  NOT NULL
);

DROP SEQUENCE WEATHER_SEQ;
CREATE SEQUENCE weather_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999 --> NUMBER(7) 대응
  CACHE 2                       -- 2번은 메모리에서만 계산
  NOCYCLE; 
  
  
INSERT INTO weather(weatherno, w_type, w_img)
VALUES (0, 'test', 'test');