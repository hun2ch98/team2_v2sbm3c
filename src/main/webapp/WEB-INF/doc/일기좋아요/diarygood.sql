-- 일기 좋아요

DROP TABLE diarygood;

CREATE TABLE diarygood (
  goodno  NUMBER(10) NOT NULL PRIMARY KEY, -- AUTO_INCREMENT 대체
  diaryno NUMBER(30)         NOT NULL,
  rdate     DATE          NOT NULL, -- 등록 날짜
  memberno    NUMBER(10)     NOT NULL , -- FK
  FOREIGN KEY (memberno) REFERENCES member (memberno),-- 일정을 등록한 관리자 
  FOREIGN KEY (diaryno) REFERENCES diary (diaryno)
);

DROP SEQUENCE diarygood_seq;

CREATE SEQUENCE diarygood_seq
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지


-- 데이터 삽입
Insert Into Diarygood(Goodno, Diaryno, Memberno, Rdate)
Values (Diarygood_Seq.Nextval, 22, 1, Sysdate);

Insert Into Diarygood(Goodno, Diaryno, Memberno, Rdate)
Values (Diarygood_Seq.Nextval, 22, 1, Sysdate);

Insert Into Diarygood(Goodno, Diaryno, Memberno, Rdate)
Values (Diarygood_Seq.Nextval, 22, 1, Sysdate);

Insert Into Diarygood(Goodno, Diaryno, Memberno, Rdate)
Values (Diarygood_Seq.Nextval, 22, 1, Sysdate);

COMMIT;

-- 전체 목록
SELECT goodno, diaryno, memberno, rdate
FROM diarygood
ORDER BY goodno DESC;

-- 조회1
SELECT goodno, rdate, diaryno, memberno
FROM diarygood
WHERE goodno = 6

-- 조회2
SELECT goodno, rdate, diaryno, memberno
FROM diarygood
WHERE goodno = 6 AND memberno = 1

-- 특정 회원의 추천 여부
SELECT COUNT(*) as cnt
FROM diarygood
WHERE diaryno = 22 AND memberno = 1;




