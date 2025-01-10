DROP TABLE score;
DROP TABLE score CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE score (
    scoreno     NUMBER(10)        NOT NULL PRIMARY KEY,
    jumsu       NUMBER(2,1)        NULL,
    rdate        DATE        NOT NULL,
    memberno NUMBER(10)        NOT NULL,
    FOREIGN KEY(memberno) REFERENCES member(memberno)
);

COMMENT ON TABLE score is '평점';
COMMENT ON COLUMN score.scoreno is '평점 번호';
COMMENT ON COLUMN score.jumsu is '평점';
COMMENT ON COLUMN score.rdate is '등록일';
COMMENT ON COLUMN score.memberno is '등록한 회원 번호';


DROP SEQUENCE score_seq;

CREATE SEQUENCE score_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
SELECT * FROM score;