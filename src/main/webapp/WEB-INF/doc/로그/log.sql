DROP TABLE log;

CREATE TABLE log(
   	logno         NUMBER(10)      NOT NULL    PRIMARY KEY,
    memberno      NUMBER(10)      NOT NULL,
    table_name    VARCHAR(20)     NOT NULL,
    action        VARCHAR(20)     NOT NULL,
    ldate         VARCHAR(10)     NOT NULL,
    details       VARCHAR2(50)    NULL,
    ip            VARCHAR(50)     NOT NULL,
    FOREIGN KEY (memberno)  REFERENCES member (memberno)
);


DROP SEQUENCE log_seq;

CREATE SEQUENCE log_seq
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지









