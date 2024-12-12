DROP TABLE aiconversation;
DROP TABLE aiconversation CASCADE CONSTRAINTS;  -- 자식 무시하고 삭제 가능

CREATE TABLE aiconversation (
    conversationno  NUMBER(10)   NOT NULL PRIMARY KEY,
    content         VARCHAR(255) NOT NULL,
    rdate           DATE         NOT NULL,
    memberno        NUMBER(10)   NOT NULL,
    summary         VARCHAR(255) NOT NULL,
    FOREIGN KEY (memberno) REFERENCES member (memberno)
);

COMMENT ON TABLE AICONVERSATION is '대화';
COMMENT ON COLUMN AICONVERSATION.CONVERSATIONNO is '대화 번호';
COMMENT ON COLUMN AICONVERSATION.MEMBERNO is '회원번호';
COMMENT ON COLUMN AICONVERSATION.CONTENT is '대화 내용';
COMMENT ON COLUMN AICONVERSATION.RDATE is '날짜';
COMMENT ON COLUMN AICONVERSATION.SUMMARY is 'AI 생성 일기';

DROP SEQUENCE aiconversation_seq;

CREATE SEQUENCE aiconversation_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;                  -- 다시 1부터 생성되는 것을 방지

