DROP TABLE emotion;
DROP TABLE emotion CASCADE CONSTRAINTS;  -- 자식 무시하고 삭제 가능

CREATE TABLE emotion (
	emno	    NUMBER(10)		NOT NULL    PRIMARY KEY,
	em_type	    VARCHAR(100)    NOT NULL,
	em_img	    VARCHAR(100)	NOT NULL
);

COMMENT ON TABLE EMOTION is '감정';
COMMENT ON COLUMN EMOTION.EMNO is '감정 번호';
COMMENT ON COLUMN EMOTION.EM_TYPE is '감정 유형';
COMMENT ON COLUMN EMOTION.EM_IMG is '감정 이미지';

DROP SEQUENCE EMOTION_SEQ;
CREATE SEQUENCE emotion_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;                  -- 다시 1부터 생성되는 것을 방지

INSERT INTO emotion(emno, em_type, em_img)
VALUES (emotion_seq.nextval, 'test', 'test');