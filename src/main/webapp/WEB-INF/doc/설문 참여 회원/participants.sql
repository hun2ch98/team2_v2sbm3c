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