DROP TABLE noticegood;

CREATE TABLE noticegood (
	noticegoodno	    NUMBER(10)	NOT NULL    PRIMARY KEY,
	rdate	            DATE	    NULL,
	memberno	        NUMBER(10)	NOT NULL,
    noticeno            NUMBER(10)  NOT NULL,
    FOREIGN KEY (memberno)  REFERENCES member (memberno),
    FOREIGN KEY (noticeno)  REFERENCES notice (noticeno)
);

DROP SEQUENCE noticegood_seq;

CREATE SEQUENCE noticegood_seq
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지

commit;

-- 데이터 삽입
INSERT INTO noticegood(noticegoodno, rdate, memberno, noticeno)
VALUES (noticegood_seq.nextval, sysdate, 1, 4);

INSERT INTO noticegood(noticegoodno, rdate, memberno, noticeno)
VALUES (noticegood_seq.nextval, sysdate, 3, 8);

INSERT INTO noticegood(noticegoodno, rdate, memberno, noticeno)
VALUES (noticegood_seq.nextval, sysdate, 4, 9);

COMMIT;

-- 전체 목록
SELECT noticegoodno, rdate, memberno, noticeno
FROM noticegood
ORDER BY noticegoodno DESC;

NOTICEGOODNO RDATE                 MEMBERNO   NOTICENO
------------ ------------------- ---------- ----------
          10 2025-01-07 06:46:13          3          8
           9 2025-01-07 06:43:58          4          9
           7 2025-01-07 06:43:58          1          4

-- PK 조회
SELECT noticegoodno, rdate, memberno, noticeno
FROM noticegood
WHERE noticegoodno = 1;

-- noticeno, memberno로 조회
SELECT noticegoodno, rdate, memberno, noticeno
FROM noticegood
WHERE noticeno=4 AND memberno=1;

-- 삭제
DELETE FROM noticegood
WHERE noticegoodno = 8;
       CNT
----------
         1

commit;

-- 특정 공지사항의 특정 회원 추천 갯수 산출
SELECT COUNT(*) as cnt
FROM noticegood
WHERE noticeno=3 AND memberno=3;

-- JOIN, 어느 공지사항을 누가 추천 했는가?
SELECT noticegoodno, rdate, memberno, noticeno
FROM noticegood
ORDER BY noticegoodno DESC;

-- 테이블 2개 join
SELECT r.noticegoodno, r.rdate, r.noticeno, c.title, r.memberno
FROM notice c, noticegood r
WHERE c.noticeno = r.noticeno
ORDER BY noticegoodno DESC;

-- 테이블 3개 join, as 사용시 컬럼명 변경 가능: c.title as n_title
SELECT r.noticegoodno, r.rdate, r.noticeno, c.title as n_title, r.memberno, m.id, m.name
FROM notice c, noticegood r, member m
WHERE c.noticeno = r.noticeno AND r.memberno = m.memberno
ORDER BY noticegoodno DESC;








