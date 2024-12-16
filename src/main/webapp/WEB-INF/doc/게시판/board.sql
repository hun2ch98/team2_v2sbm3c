DROP TABLE board;
DROP TABLE board CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE board(
    boardno         NUMBER(10)      NOT NULL    PRIMARY KEY,
    memberno        NUMBER(10)      NOT NULL,
    bcontent        CLOB            NOT NULL,
    rdate           DATE            NOT NULL,
    board_cate      NUMBER(10)      NOT NULL,
    file1            VARCHAR(200)        NULL,
    FOREIGN KEY (memberno)  REFERENCES member (memberno)
);

COMMENT ON TABLE  BOARD is '게시판';
COMMENT ON COLUMN BOARD.BOARDNO is '게시판 번호';
COMMENT ON COLUMN BOARD.MEMBERNO is '회원 번호';
COMMENT ON COLUMN BOARD.BCONTENT is '게시글 내용';
COMMENT ON COLUMN BOARD.RDATE is '날짜';
COMMENT ON COLUMN BOARD.BOARD_CATE is '게시판 종류';
COMMENT ON COLUMN BOARD.FILE1 is '파일 업로드';

DROP SEQUENCE board_seq;

CREATE SEQUENCE board_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;                  -- 다시 1부터 생성되는 것을 방지

