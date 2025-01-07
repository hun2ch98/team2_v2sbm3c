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
VALUES (noticegood_seq.nextval, sysdate, 1, 1);

INSERT INTO noticegood(noticegoodno, rdate, memberno, noticeno)
VALUES (noticegood_seq.nextval, sysdate, 3, 3);

INSERT INTO noticegood(noticegoodno, rdate, memberno, noticeno)
VALUES (noticegood_seq.nextval, sysdate, 4, 4);

COMMIT;

-- 전체 목록
SELECT noticegoodno, rdate, memberno, noticeno
FROM noticegood
ORDER BY noticegoodno DESC;

NOTICEGOODNO RDATE                 MEMBERNO   NOTICENO
------------ ------------------- ---------- ----------
           3 2025-01-07 10:57:56          4          4
           2 2025-01-07 10:57:56          3          3
           1 2025-01-07 10:55:47          1          1

-- 조회
SELECT noticegoodno, rdate, memberno, noticeno
FROM noticegood
WHERE noticegoodno = 1;

-- 삭제
DELETE FROM noticegood
WHERE noticegoodno = 3;
       CNT
----------
         1

commit;

SELECT COUNT(*) as cnt
FROM noticegood
WHERE noticeno=3 AND memberno=3;

