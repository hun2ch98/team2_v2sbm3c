/**********************************/
/* Table Name: 설문 조사 항목 */
/**********************************/

-- 테이블 삭제
DROP TABLE surveyitem;
DROP TABLE surveyitem CASCADE CONSTRAINTS;

CREATE TABLE surveyitem (
    itemno      NUMBER(10)      NOT NULL    PRIMARY KEY,
    surveyno    NUMBER(10)      NOT NULL,
    memberno    NUMBER(10)      NOT NULL,
    item_seq    NUMBER(5)       NOT NULL,
    item        VARCHAR2(200)   NOT NULL,
    item_cnt    NUMBER(7)           NULL,
    FOREIGN KEY (surveyno)  REFERENCES survey (surveyno),
    FOREIGN KEY (memberno)  REFERENCES member (memberno)
);
ALTER TABLE surveyitem
MODIFY item_cnt NUMBER DEFAULT 0;


COMMENT ON TABLE SURVEYITEM is '설문 조사 항목';
COMMENT ON COLUMN SURVEYITEM.ITEMNO is '설문 조사 항목 번호';
COMMENT ON COLUMN SURVEYITEM.SURVEYNO is '설문 조사 번호';
COMMENT ON COLUMN SURVEYITEM.MEMBERNO is '회원 번호';
COMMENT ON COLUMN SURVEYITEM.ITEM_SEQ is '항목 출력 순서';
COMMENT ON COLUMN SURVEYITEM.ITEM is '항목';
COMMENT ON COLUMN SURVEYITEM.ITEM_CNT is '항목 선택 인원';

DROP SEQUENCE surveyitem_seq;

CREATE SEQUENCE surveyitem_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;  

COMMIT;

-- 등록
INSERT INTO surveyitem(itemno, memberno, surveyno, item_seq, item)
VALUES (surveyitem_seq.nextval, 1, 12, 1, '이 서비스를 일주일에 얼마나 사용하나요?');
INSERT INTO surveyitem(itemno, memberno, surveyno, item_seq, item)
VALUES (surveyitem_seq.nextval, 1, 12, 2, '이 서비스를 자주 이용하는데 어려움이 있나요?');
INSERT INTO surveyitem(itemno, memberno, surveyno, item_seq, item)
VALUES (surveyitem_seq.nextval, 1, 12, 3, '이 서비스를 아이가 자주 찾나요?');
INSERT INTO surveyitem(itemno, memberno, surveyno, item_seq, item)
VALUES (surveyitem_seq.nextval, 1, 12, 4, '자주 사용하는 기능은 무엇인가요?');

INSERT INTO surveyitem(itemno, memberno, surveyno, item_seq, item)
VALUES (surveyitem_seq.nextval, 1, 2, 1, '사용되는 언어와 표현이 적절하다고 느끼셨나요?');
INSERT INTO surveyitem(itemno, memberno, surveyno, item_seq, item)
VALUES (surveyitem_seq.nextval, 1, 2, 2, '아이에게 불쾌감을 줄 수 있는 표현이 있었나요?');
INSERT INTO surveyitem(itemno, memberno, surveyno, item_seq, item)
VALUES (surveyitem_seq.nextval, 1, 2, 3, '사용된 문구가 아이들에게 친근하고 이해하기 쉬웠나요?');
INSERT INTO surveyitem(itemno, memberno, surveyno, item_seq, item)
VALUES (surveyitem_seq.nextval, 1, 2, 4, '아이가 사용된 표현에 대해 부정적인 반응을 보인 적이 있나요?');

INSERT INTO surveyitem(itemno, memberno, surveyno, item_seq, item)
VALUES (surveyitem_seq.nextval, 1, 14, 1, '테스트항목');
INSERT INTO surveyitem(itemno, memberno, surveyno, item_seq, item)
VALUES (surveyitem_seq.nextval, 1, 14, 2, '테스트항목');
INSERT INTO surveyitem(itemno, memberno, surveyno, item_seq, item)
VALUES (surveyitem_seq.nextval, 1, 14, 3, '테스트항목');
INSERT INTO surveyitem(itemno, memberno, surveyno, item_seq, item)
VALUES (surveyitem_seq.nextval, 1, 14, 4, '테스트항목');

-- 조회
SELECT * FROM surveyitem;
    ITEMNO   SURVEYNO   ITEM_SEQ ITEM                                                                                                                                                                                                       ITEM_CNT
---------- ---------- ---------- -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- ----------
         1         31          1 이 서비스를 일주일에 5회 이상 사용한다.                                                                                                                                                                          10

-- 수정
UPDATE surveyitem SET item='이 서비스를 일주일에 2회 이하 사용한다.' WHERE itemno=1;
    ITEMNO   SURVEYNO   ITEM_SEQ ITEM                                                                                                                                                                                                       ITEM_CNT
---------- ---------- ---------- -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- ----------
         1         31          1 이 서비스를 일주일에 2회 이하 사용한다.                                                                                                                                                                          10

UPDATE surveyitem SET item_seq='5' WHERE itemno=9;
-- 삭제
DELETE FROM surveyitem;

UPDATE surveyitem
SET item_cnt = 0
WHERE itemno = 17;
SELECT itemno, surveyno, item_seq, item, item_cnt
    FROM surveyitem
    WHERE itemno = 1;

COMMIT;
SELECT * FROM surveyitem;
-- 검색
SELECT itemno, surveyno, item_seq, item
FROM surveyitem
WHERE (UPPER(item) LIKE '%' || UPPER('서비스') || '%')
ORDER BY itemno ASC;

-- 검색 갯수
SELECT COUNT(*) as cnt
FROM surveyitem
WHERE (UPPER(item) LIKE '%' || UPPER('서비스') || '%');

-- ③ 정렬 -> ROWNUM -> 분할
SELECT itemno, surveyno, item_seq, item, r
FROM (
    SELECT itemno, surveyno, item_seq, item, rownum as r
    FROM (
        SELECT itemno, surveyno, item_seq, item
        FROM surveyitem
        WHERE (UPPER(item) LIKE '%' || UPPER('서비스') || '%')
        ORDER BY itemno ASC
    )
)
WHERE r >= 1 AND r <= 2;

SELECT COUNT(*) 
    FROM participants 
    WHERE itemno = 24 AND memberno = 2;
  COUNT(*)
----------
         2


-- JOIN
SELECT survey.surveyno, survey.topic,
        surveyitem.itemno, surveyitem.item, surveyitem.item_seq, surveyitem.item_cnt
FROM survey s, surveyitem i
ORDER BY itemno ASC;

SELECT s.surveyno, s.topic,
        i.itemno, i.item, i.item_seq, i.item_cnt
FROM survey s, surveyitem i
WHERE s.surveyno = i.surveyno
ORDER BY itemno ASC;

SELECT s.surveyno, s.topic as s.surveyno
        i.surveyno, i.itemno, i.item, i.item_seq, i.item_cnt as i.surveyno
FROM survey s, surveyitem i
WHERE (s.surveyno = i.surveyno) AND s.topic='사용 빈도'
ORDER BY itemno ASC;
        
SELECT 
    s.surveyno AS survey_number, 
    s.topic AS topic_name,
    i.surveyno AS item_survey_number, 
    i.itemno AS item_number, 
    i.item AS item_name, 
    i.item_seq AS item_sequence, 
    i.item_cnt AS item_count
FROM 
    survey s
JOIN 
    surveyitem i
ON 
    s.surveyno = i.surveyno
WHERE 
    s.topic = '사용 빈도'
ORDER BY 
    i.itemno ASC;

DELETE FROM surveyitem
WHERE surveyno=14;

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
        
