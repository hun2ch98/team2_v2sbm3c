DROP TABLE notice;

CREATE TABLE notice(
    noticeno    NUMBER(10)      NOT NULL PRIMARY KEY,
    memberno    NUMBER(10)      NOT NULL,
    title       VARCHAR(300)    NOT NULL,
    content     VARCHAR(4000)   NOT NULL,
    cnt         NUMBER(7)       NOT NULL,
    rdate       DATE            NOT NULL,
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
