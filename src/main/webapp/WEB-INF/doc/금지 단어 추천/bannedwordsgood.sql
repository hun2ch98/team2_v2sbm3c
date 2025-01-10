DROP TABLE bannedwordsgood;
DROP TABLE bannedwordsgood CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE bannedwordsgood (
	goodno	    NUMBER(10)		NOT NULL,
	rdate	    DATE		    NOT NULL,
	wordno	    NUMBER(10)		NOT NULL,
	memberno	NUMBER(10)		NOT NULL
);

COMMENT ON TABLE bannedwordsgood is '금지단어 추천';
COMMENT ON COLUMN bannedwordsgood.goodno is '금지단어 추천 번호';
COMMENT ON COLUMN bannedwordsgood.rdate is '등록일';
COMMENT ON COLUMN bannedwordsgood.wordno is '금지단어 번호';
COMMENT ON COLUMN bannedwordsgood.memberno is '회원 번호';

DROP SEQUENCE bannedwordsgood_seq;

CREATE SEQUENCE bannedwordsgood_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
SELECT * FROM bannedwordsgood;

COMMIT;

INSERT INTO bannedwordsgood(goodno, rdate, wordno, memberno)
VALUES(bannedwordsgood_seq.nextval, sysdate, '1', 1);

INSERT INTO bannedwordsgood(goodno, rdate, wordno, memberno)
VALUES(bannedwordsgood_seq.nextval, sysdate, '2', 2);

INSERT INTO bannedwordsgood(goodno, rdate, wordno, memberno)
VALUES(bannedwordsgood_seq.nextval, sysdate, '1', 2);

INSERT INTO bannedwordsgood(goodno, rdate, wordno, memberno)
VALUES(bannedwordsgood_seq.nextval, sysdate, '1', 3);

INSERT INTO bannedwordsgood(goodno, rdate, wordno, memberno)
VALUES(bannedwordsgood_seq.nextval, sysdate, '1', 4);

INSERT INTO bannedwordsgood(goodno, rdate, wordno, memberno)
VALUES(bannedwordsgood_seq.nextval, sysdate, '1', 1);


SELECT goodno, rdate, wordno, memberno
FROM bannedwordsgood
ORDER BY goodno DESC;

DELETE FROM bannedwordsgood
WHERE goodno = 6;

SELECT COUNT(*) as cnt 
FROM bannedwordsgood
WHERE goodno=1 AND memberno=1;

SELECT COUNT(*) as cnt 
FROM bannedwordsgood
WHERE goodno=1 AND memberno=4;

-- 조회
SELECT goodno, rdate, wordno, memberno
FROM bannedwordsgood
WHERE goodno = 1;
-- wordno, memberno로 조회 
SELECT goodno, rdate, wordno, memberno
FROM bannedwordsgood
WHERE wordno = 1 AND memberno=1;

SELECT g.goodno, g.rdate, g.wordno, b.word as b_title, g.memberno, m.id, m.name
FROM bannedwords b, bannedwordsgood g, member m
WHERE b.wordno = g.wordno AND g.memberno = m.memberno
ORDER BY goodno DESC;







