DROP TABLE loginlog;

CREATE TABLE loginlog (
	loginlogno   NUMBER(10)	                    NOT NULL    PRIMARY KEY,
    id           VARCHAR(30)                    NOT NULL,
    ip           VARCHAR(15)                    NOT NULL,
    result       VARCHAR(1)      DEFAULT 'F'    NOT NULL,
    ldate        DATE                           NOT NULL
);

DROP SEQUENCE loginlog_seq;

CREATE SEQUENCE loginlog_seq
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지

commit;

-- 전체 목록
SELECT loginlogno, id, ip, result, ldate
FROM loginlog
ORDER BY loginlogno DESC;