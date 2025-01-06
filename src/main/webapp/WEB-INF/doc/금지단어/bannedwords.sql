DROP TABLE bannedwords;
DROP TABLE bannedwords CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE bannedwords (
	wordno	    NUMBER(10)		    NOT NULL,
	word	    VARCHAR(50)		NOT NULL,
	reason	    VARCHAR(50)		NOT NULL,
	rdate	    DATE		        NOT NULL
);

COMMENT ON TABLE bannedwords is '금지단어';
COMMENT ON COLUMN bannedwords.wordno is '금지단어 번호';
COMMENT ON COLUMN bannedwords.word is '금지단어';
COMMENT ON COLUMN bannedwords.reason is '이유';
COMMENT ON COLUMN bannedwords.rdate is '금지단어 등록일';

DROP SEQUENCE bannedwords_seq;

CREATE SEQUENCE bannedwords_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
SELECT * FROM bannedwords;

COMMIT;
  