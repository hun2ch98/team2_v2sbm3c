/**********************************/
/* Table Name: 설문 참여 회원 */
/**********************************/
-- 테이블 삭제
DROP TABLE participants;

CREATE TABLE participants(
    pno                NUMBER(10)   NOT NULL    PRIMARY KEY,
    pdate              DATE         NOT NULL,
    itemno             NUMBER(10)       NULL ,
    memberno           NUMBER(6)        NULL ,
    FOREIGN KEY (itemno) REFERENCES surveyitem (itemno),
    FOREIGN KEY (memberno) REFERENCES MEMBER (memberno)
);

COMMENT ON TABLE participants is '설문 참여 회원';
COMMENT ON COLUMN participants.pno is '설문 참여 회원 번호';
COMMENT ON COLUMN participants.itemno is '설문 조사 항목 번호';
COMMENT ON COLUMN participants.memberno is '회원 번호';
COMMENT ON COLUMN participants.pdate is '설문 참여 날짜';

DROP SEQUENCE participants_seq;

CREATE SEQUENCE participants_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;  

COMMIT;

-- 데이터 삽입
INSERT INTO participants(pno, itemno, memberno, pdate)
VALUES (participants_seq.nextval, 1, 2, sysdate);

INSERT INTO participants(pno, itemno, memberno, pdate)
VALUES (participants_seq.nextval, 2, 2, sysdate);

INSERT INTO participants(pno, itemno, memberno, pdate)
VALUES (participants_seq.nextval, 3, 2, sysdate);

INSERT INTO participants(pno, itemno, memberno, pdate)
VALUES (participants_seq.nextval, 5, 2, sysdate);

COMMIT;

-- 전체 목록
SELECT pno, itemno, memberno, pdate
FROM participants
ORDER BY pno DESC;

-- 조회
SELECT pno, itemno, memberno, pdate
FROM participants
WHERE pno = 1;
   GOODNO   SURVEYNO   MEMBERNO RDATE            
---------- ---------- ---------- -----------------
         1          1          1 25/01/07 10:58:38

-- 삭제
DELETE FROM participants
WHERE pno = 5;
  GOODNO   SURVEYNO   MEMBERNO RDATE            
---------- ---------- ---------- -----------------
         3          3          3 25/01/07 10:58:38
         2          2          2 25/01/07 10:58:38
         1          1          1 25/01/07 10:58:38

COMMIT;

SELECT COUNT (*) as cnt
FROM participants
WHERE itemno=1 AND memberno =2;
       CNT
----------
         0

SELECT COUNT (*) as cnt
FROM participants
WHERE surveyno=1 AND memberno =1;
      CNT
----------
         1
         
-- memberno FK 특정 관리자에 속한 레코드 모두 삭제
DELETE FROM surveyitem
WHERE memberno=1;         
         
-- JOIN, 어느 공지사항을 누가 추천 했는가?
SELECT pno, itemno, memberno, pdate
FROM participants
ORDER BY pno DESC;

-- 테이블 2개 join
SELECT i.itemno, i.surveyno, i.item_seq, i.item, i.item_cnt, s.topic
FROM survey s, surveyitem i
WHERE s.surveyno = i.surveyno
ORDER BY itemno DESC;

-- 테이블 3개 join, as 사용시 컬럼명 변경 가능: c.title as n_title
SELECT p.pno, p.itemno, p.memberno, p.pdate, i.item as i_item, m.memberno, m.email, m.name
FROM surveyitem i, participants p, member m
WHERE i.itemno = p.itemno AND p.memberno = m.memberno
ORDER BY pno DESC;         
         
         
         