-- 설문조사 추천

DROP TABLE surveygood;

CREATE TABLE surveygood (
  goodno        NUMBER(10) NOT NULL PRIMARY KEY, -- AUTO_INCREMENT 대체
  surveyno      NUMBER(10)         NOT NULL,
  rdate         DATE          NOT NULL, -- 등록 날짜
  memberno      NUMBER(10)     NOT NULL , -- FK
  FOREIGN KEY (memberno) REFERENCES member (memberno),-- 일정을 등록한 관리자 
  FOREIGN KEY (surveyno) REFERENCES survey (surveyno) ON DELETE CASCADE
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
VALUES (surveygood_seq.nextval, 2, 2, sysdate);

INSERT INTO surveygood(goodno, surveyno, memberno, rdate)
VALUES (surveygood_seq.nextval, 3, 3, sysdate);

INSERT INTO surveygood(goodno, surveyno, memberno, rdate)
VALUES (surveygood_seq.nextval, 4, 4, sysdate);

COMMIT;

-- 전체 목록
SELECT goodno, surveyno, memberno, rdate
FROM surveygood
ORDER BY goodno DESC;

-- 조회
SELECT goodno, surveyno, memberno, rdate
FROM surveygood
WHERE goodno = 1;
   GOODNO   SURVEYNO   MEMBERNO RDATE            
---------- ---------- ---------- -----------------
         1          1          1 25/01/07 10:58:38

-- 삭제
DELETE FROM surveygood;

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

-- JOIN, 어느 공지사항을 누가 추천 했는가?
SELECT itemno, surveyno, item_seq, item, item_cnt
FROM noticegood
ORDER BY noticegoodno DESC;

-- 테이블 2개 join
SELECT i.itemno, i.surveyno, i.item_seq, i.item, i.item_cnt, s.topic
FROM survey s, surveyitem i
WHERE s.surveyno = i.surveyno
ORDER BY itemno DESC;

-- 테이블 3개 join, as 사용시 컬럼명 변경 가능: c.title as n_title
SELECT i.itemno, i.surveyno, i.item_seq, i.item, i.item_cnt, s.topic as s_topic, m.memberno, m.id, m.name
FROM notice c, noticegood r, member m
WHERE c.noticeno = r.noticeno AND r.memberno = m.memberno
ORDER BY noticegoodno DESC;


-- 검색된 데이터의 총 개수 계산

SELECT COUNT(*) AS cnt
FROM surveygood g
JOIN survey s ON g.surveyno = s.surveyno
JOIN member m ON g.memberno = m.memberno
WHERE UPPER(m.id) LIKE '%' || UPPER('test') || '%'  
        OR UPPER(s.topic) LIKE '%' || UPPER('서비스') || '%';



-- 조인 + 검색 + 페이징
SELECT *
FROM (
    SELECT 
        g.goodno, 
        g.surveyno, 
        g.memberno, 
        g.rdate, 
        s.topic AS s_topic, 
        m.id AS id, 
        ROW_NUMBER() OVER (ORDER BY g.goodno ASC) AS r
    FROM surveygood g
    JOIN survey s ON g.surveyno = s.surveyno
    JOIN member m ON g.memberno = m.memberno
    WHERE UPPER(m.id) LIKE '%' || UPPER('test') || '%'  
        OR UPPER(s.topic) LIKE '%' || UPPER('서비스') || '%'
)
WHERE r >= 1 AND r <= 2;

   GOODNO   SURVEYNO   MEMBERNO RDATE             S_TOPIC                                                                                              ID                                                          R
---------- ---------- ---------- ----------------- ---------------------------------------------------------------------------------------------------- -------------------------------------------------- ----------
         1          2          1 25/01/14 10:17:02 이 서비스를 자주 이용하는데 어려움이 있나요?                                                         admin                                                       1
         6          1          2 25/01/15 12:49:07 이 서비스를 일주일에 얼마나 사용하나요?                                                              test                                                        2

