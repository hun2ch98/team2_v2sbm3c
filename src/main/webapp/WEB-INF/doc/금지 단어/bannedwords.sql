DROP TABLE bannedwords;
DROP TABLE bannedwords CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE bannedwords (
	wordno	    NUMBER(10)		        NOT NULL,
	word	    VARCHAR(100)		    NOT NULL,
    reason      VARCHAR(100)            NOT NULL,
    goodcnt     NUMBER(7)               NULL,
	rdate       DATE    NOT NULL
);

COMMENT ON TABLE bannedwords is '금지단어';
COMMENT ON COLUMN bannedwords.wordno is '금지단어 번호';
COMMENT ON COLUMN bannedwords.word is '금지단어';
COMMENT ON COLUMN bannedwords.reason is '금지단어 이유';
COMMENT ON COLUMN bannedwords.goodcnt is '금지단어 좋아요 수';
COMMENT ON COLUMN bannedwords.rdate is '업로드 날짜';

DROP SEQUENCE bannedwords_seq;

CREATE SEQUENCE bannedwords_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지

COMMIT;

SELECT * FROM bannedwords;

INSERT INTO bannedwords(wordno, word, reason, rdate)
VALUES(bannedwords_seq.nextval, '시발', '비속어', sysdate);