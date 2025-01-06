DROP TABLE notice;

CREATE TABLE notice(
    noticeno    NUMBER(10)                  NOT NULL PRIMARY KEY,
    memberno    NUMBER(10)                  NOT NULL,
    title       VARCHAR(300)                NOT NULL,
    content     VARCHAR(4000)               NOT NULL,
    cnt         NUMBER(7)                   NOT NULL,
    rdate       DATE                        NOT NULL,
    FOREIGN KEY (memberno)  REFERENCES member (memberno)
);

COMMENT ON TABLE NOTICE is '공지사항';
COMMENT ON COLUMN NOTICE.NOTICENO is '공지사항 번호';
COMMENT ON COLUMN NOTICE.MEMBERNO is '회원 번호';
COMMENT ON COLUMN NOTICE.TITLE is '공지사항 제목';
COMMENT ON COLUMN NOTICE.CONTENT is '공지사항 내용';
COMMENT ON COLUMN NOTICE.CNT is '조회수';
COMMENT ON COLUMN NOTICE.RDATE is '등록일';

commit;

DROP SEQUENCE NOTICE_SEQ;

CREATE SEQUENCE NOTICE_SEQ
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지

-- CRUD
-- 등록 -> Create
INSERT INTO notice(noticeno, memberno, title, content, cnt, rdate)
VALUES (notice_seq.nextval, 1, '긴급 공지!', '오늘은 날씨가 갑자기 확 추워지면서, 독감이 유행하고있습니다.. 모두 독감을 조심하시고 건강한 하루가 되시길 바랍니다.', 0, sysdate);

commit;

-- 목록 -> List
SELECT noticeno, memberno, title, content, cnt, rdate FROM notice ORDER BY noticeno ASC;
  NOTICENO  MEMBERNO     TITLE       CONTENT                       CNT       RDATE
---------- ----------   -------      --------                   ---------   ------
    1          1        긴급 공지!  오늘은 날씨가 갑자기 확 추워지면서,    0       2025-01-06 03:50:42
                                   독감이 유행하고있습니다.. 
                                   모두 독감을 조심하시고 
                                   건강한 하루가 되시길 바랍니다.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
-- 조회 -> Read
SELECT noticeno, memberno, title, content, cnt, rdate FROM notice WHERE noticeno=1;
  NOTICENO   MEMBERNO TITLE                                      CONTENT                                                                CNT            RDATE 
---------- ---------- ----------  --------------------------------------------------------------------------------------------------  --------   -------------------
         1          1 긴급 공지!    오늘은 날씨가 갑자기 확 추워지면서, 독감이 유행하고있습니다.. 모두 독감을 조심하시고 건강한 하루가 되시길 바랍니다.     0       2025-01-06 03:50:42

-- 수정 -> Update
UPDATE notice SET title='공지 사항!', content = '그림 일기는 만7세 어린 아이를 위한 일기입니다.', cnt=10, rdate=sysdate WHERE noticeno=1;

commit;

SELECT noticeno, memberno, title, content, cnt, rdate
FROM notice
WHERE noticeno=1;

-- 삭제 -> Delete
DELETE FROM notice WHERE noticeno=1;
SELECT noticeno, memberno, title, content, cnt, rdate FROM notice ORDER BY noticeno ASC;
