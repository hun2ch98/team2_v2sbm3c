/**********************************/
/* Table Name: 설문 조사 항목 */
/**********************************/

-- 테이블 삭제
DROP TABLE survey;

CREATE TABLE surveyitem (
    itemno      NUMBER(10)      NOT NULL,
    surveyno    NUMBER(10)      NOT NULL,
    item_seq    NUMBER(5)       NOT NULL,
    item        VARCHAR2(200)   NOT NULL,
    item_cnt    NUMBER(7)       NULL,
    FOREIGN KEY (surveyno) REFERENCES survey (surveyno)
);


COMMENT ON TABLE SURVEYITEM is '설문 조사 항목';
COMMENT ON COLUMN SURVEYITEM.ITEMNO is '설문 조사 항목 번호';
COMMENT ON COLUMN SURVEYITEM.SURVEYNO is '설문 조사 번호';
COMMENT ON COLUMN SURVEYITEM.ITEM_SEQ is '항목 출력 순서';
COMMENT ON COLUMN SURVEYITEM.ITEM is '항목';
COMMENT ON COLUMN SURVEYITEM.ITEM_CNT is '항목 선택 인원';

COMMIT;