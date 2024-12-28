DROP TABLE board;
DROP TABLE board CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE board(
    boardno         NUMBER(10)      NOT NULL    PRIMARY KEY,
    memberno        NUMBER(10)      NOT NULL,
    title           VARCHAR(100)     NOT NULL,
    bcontent        CLOB            NOT NULL,
    rdate           DATE            NOT NULL,
    board_cate      VARCHAR(50)     NOT NULL,
    file1           VARCHAR(200)        NULL,
    file1saved      VARCHAR2(100)		 NULL,
	thumb1          VARCHAR2(100)		 NULL,
	size1           NUMBER(10)		     NULL,
    goodcnt         NUMBER(10)          NULL,
    badcnt         NUMBER(10)          NULL,
    FOREIGN KEY (memberno)  REFERENCES member (memberno)
);

ALTER TABLE BOARD MODIFY TITLE VARCHAR(100);

COMMENT ON TABLE  BOARD is '게시판';
COMMENT ON COLUMN BOARD.BOARDNO is '게시판 번호';
COMMENT ON COLUMN BOARD.MEMBERNO is '회원 번호';
COMMENT ON COLUMN BOARD.TITLE is '제목';
COMMENT ON COLUMN BOARD.BCONTENT is '게시글 내용';
COMMENT ON COLUMN BOARD.RDATE is '날짜';
COMMENT ON COLUMN BOARD.BOARD_CATE is '게시판 종류';
COMMENT ON COLUMN BOARD.FILE1 is '파일 업로드';
COMMENT ON COLUMN BOARD.FILE1SAVED is '실제 저장된 메인 이미지';
COMMENT ON COLUMN BOARD.THUMB1 is '메인 이미지 Preview';
COMMENT ON COLUMN BOARD.SIZE1 is '메인 이미지 크기';
COMMENT ON COLUMN BOARD.GOODCNT is '추천수';
COMMENT ON COLUMN BOARD.BADCNT is '비추천';

DROP SEQUENCE board_seq;

CREATE SEQUENCE board_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;                  -- 다시 1부터 생성되는 것을 방지

COMMIT;

SELECT * FROM board;

SELECT boardno, memberno, title, bcontent, rdate, board_cate, LOWER(file1) as file1, file1saved, thumb1, size1
FROM board
WHERE boardno=5
ORDER BY boardno ASC;

DELETE FROM board;

DELETE FROM board WHERE boardno=74;

-- ----------------------------------------------------------------------------------------------------
-- 검색, board_cate 검색 목록
-- ----------------------------------------------------------------------------------------------------

-- 1) 검색
SELECT boardno, memberno, title, bcontent, rdate, board_cate, LOWER(file1) as file1, file1saved, thumb1, size1
FROM board
WHERE board_cate = '금지 단어'
ORDER BY boardno DESC;

SELECT boardno, memberno, title, bcontent, rdate, board_cate, LOWER(file1) as file1, file1saved, thumb1, size1
FROM board
WHERE board_cate = '문의사항'
ORDER BY boardno DESC;

-----------------------------------------------------
레코드 수
-----------------------------------------------------
SELECT COUNT(*)
FROM board
WHERE board_cate='금지 단어';

  COUNT(*)
----------
         2
         
SELECT COUNT(*)
FROM board
WHERE board_cate='문의사항';

 COUNT(*)
----------
         3

-- ----------------------------------------------------------------------------------------------------
-- 검색 + 페이징 + 메인 이미지
-- ----------------------------------------------------------------------------------------------------
SELECT *
FROM (
    SELECT boardno, memberno, title, bcontent, rdate, board_cate, LOWER(file1) as file1, file1saved, thumb1, size1, ROWNUM AS rnum
    FROM (
        SELECT boardno, memberno, title, bcontent, rdate, board_cate, LOWER(file1) as file1, file1saved, thumb1, size1
        FROM board
        WHERE board_cate = '문의사항'
        ORDER BY boardno DESC
    )
    WHERE ROWNUM <= 5 -- 여기서 상위 2개까지 가져옴
)
WHERE rnum >= 1; -- 여기서 1~10번 행만 가져옴

COMMIT;

SELECT * FROM board;
-- 출력 우선순위 낮춤
UPDATE board SET goodcnt=goodcnt+1 WHERE boardno=78;
UPDATE board SET badcnt=badcnt+1 WHERE boardno=79;








