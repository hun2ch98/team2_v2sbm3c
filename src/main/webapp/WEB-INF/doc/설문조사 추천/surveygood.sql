-- 설문조사 추천

DROP TABLE surveygood;

CREATE TABLE surveygood (
  goodno        NUMBER(10) NOT NULL PRIMARY KEY, -- AUTO_INCREMENT 대체
  surveyno      NUMBER(10)         NOT NULL,
  rdate         DATE          NOT NULL, -- 등록 날짜
  memberno      NUMBER(10)     NOT NULL , -- FK
  FOREIGN KEY (memberno) REFERENCES member (memberno),-- 일정을 등록한 관리자 
  FOREIGN KEY (surveyno) REFERENCES survey (surveyno)
);

DROP SEQUENCE surveygood_seq;

CREATE SEQUENCE surveygood_seq
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;    

-- 데이터 삽입
INSERT INTO surveygood(goodno, surveyno, memberno, rdate)
VALUES (surveygood_seq.nextval, 1, 1, sysdate);

INSERT INTO surveygood(goodno, surveyno, memberno, rdate)
VALUES (surveygood_seq.nextval, 12, 2, sysdate);

INSERT INTO surveygood(goodno, surveyno, memberno, rdate)
VALUES (surveygood_seq.nextval, 3, 3, sysdate);

INSERT INTO surveygood(goodno, surveyno, memberno, rdate)
VALUES (surveygood_seq.nextval, 4, 4, sysdate);

COMMIT;

-- 전체 목록
SELECT goodno, surveyno, memberno, rdate
FROM surveygood
ORDER BY goodno DESC;

-- PK 조회
SELECT goodno, surveyno, memberno, rdate
FROM surveygood
WHERE goodno = 5;

-- surveyno, memberno로 조회
SELECT goodno, surveyno, memberno, rdate
FROM surveygood
WHERE surveyno=12 AND memberno =2;

-- 삭제
DELETE FROM surveygood
WHERE goodno = 4;
  GOODNO   SURVEYNO   MEMBERNO RDATE            
---------- ---------- ---------- -----------------
         3          3          3 25/01/07 10:58:38
         2          2          2 25/01/07 10:58:38
         1          1          1 25/01/07 10:58:38

COMMIT;

SELECT COUNT (*) as cnt
FROM surveygood
WHERE surveyno=1 AND memberno =2;
       CNT
----------
         0

SELECT COUNT (*) as cnt
FROM surveygood
WHERE surveyno=1 AND memberno =1;
      CNT
----------
         1



