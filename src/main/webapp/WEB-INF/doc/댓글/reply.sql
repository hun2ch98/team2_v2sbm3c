DROP TABLE reply;
DROP TABLE reply CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE reply (
	replyno	    NUMBER(10)		NOT NULL,
	rcontent	VARCHAR(100)		NOT NULL,
	rdate	    DATE		NOT NULL,
	memberno	NUMBER(10)		NOT NULL,
	boardno	    NUMBER(10)		NOT NULL,
	fixed_at	DATE		NOT NULL,
	passwd	    VARCHAR(10)		NOT NULL,
	recom	    NUMBER(10)		NULL
);

COMMENT ON TABLE reply is '댓글';
COMMENT ON COLUMN reply.replyno is '댓글 번호';
COMMENT ON COLUMN reply.rcontent is '댓글 내용';
COMMENT ON COLUMN reply.rdate is '댓글 등록일';
COMMENT ON COLUMN reply.memberno is '회원 번호';
COMMENT ON COLUMN reply.boardno is '게시글 번호';
COMMENT ON COLUMN reply.fixed_at is '수정일';
COMMENT ON COLUMN reply.passwd is '패스워드';
COMMENT ON COLUMN reply.recom is '추천수';

DROP SEQUENCE reply_seq;

CREATE SEQUENCE reply_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
INSERT INTO reply(replyno, rcontent, rdate, memberno, boardno, fixed_at, passwd, recom)
VALUES(reply_seq.nextval, '댓글 남겨요', sysdate, 1, 1, sysdate, '1234', 1);

INSERT INTO reply(replyno, rcontent, rdate, memberno, boardno, fixed_at, passwd, recom)
VALUES(reply_seq.nextval, '댓글 남겨요', sysdate, 1, 17, sysdate, '1234', 1);

SELECT * FROM reply;

COMMIT;




