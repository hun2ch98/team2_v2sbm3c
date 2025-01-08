DROP TABLE learningdata;
DROP TABLE learningdata CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE learningdata (
	datano	    NUMBER(10)		    NOT NULL,
	ethical	    VARCHAR(1)	    NOT	NULL,
	create_at	DATE		NOT NULL,
	ques	    VARCHAR(100)		NOT NULL,
	ans	        VARCHAR(100)		NOT NULL
);

COMMENT ON TABLE learningdata is '학습 데이터';
COMMENT ON COLUMN learningdata.datano is '학습 데이터 번호';
COMMENT ON COLUMN learningdata.ethical is '도덕성 검증 여부';
COMMENT ON COLUMN learningdata.create_at is '데이터 등록일';
COMMENT ON COLUMN learningdata.ques is '학습 데이터 질문';
COMMENT ON COLUMN learningdata.ans is '학습 데이터 답변';

DROP SEQUENCE learningdata_seq;

CREATE SEQUENCE learningdata_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
SELECT * FROM learningdata;

INSERT INTO learningdata(datano, ethical, create_at, ques, ans)
VALUES(learningdata_seq.nextval,'Y' ,sysdate, '오늘 뭐했어?', '맛있는 밥 먹었어.');

COMMIT;