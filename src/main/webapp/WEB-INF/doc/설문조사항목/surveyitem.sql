/**********************************/
/* Table Name: 설문 조사 항목 */
/**********************************/

-- 테이블 삭제
DROP TABLE surveyitem;

CREATE TABLE surveyitem (
    itemno      NUMBER(10)      NOT NULL,
    surveyno    NUMBER(10)      NOT NULL,
    item_seq    NUMBER(5)       NOT NULL,
    item        VARCHAR2(200)   NOT NULL,
    item_cnt    NUMBER(7)           NULL,
    FOREIGN KEY (surveyno) REFERENCES survey (surveyno)
);


COMMENT ON TABLE SURVEYITEM is '설문 조사 항목';
COMMENT ON COLUMN SURVEYITEM.ITEMNO is '설문 조사 항목 번호';
COMMENT ON COLUMN SURVEYITEM.SURVEYNO is '설문 조사 번호';
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
INSERT INTO surveyitem(itemno, surveyno, item_seq, item, item_cnt)
VALUES (surveyitem_seq.nextval, 31, 1, '이 서비스를 일주일에 5회 이상 사용한다.', 10);

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


-- 삭제
DELETE FROM surveyitem;

COMMIT;