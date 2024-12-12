DROP TABLE learningdata;

CREATE TABLE learningdata (
	datano	    NUMBER(100)		    NOT NULL,
	data_type	VARCHAR(100)	    NOT	NULL,
	ethical	    CHAR(1)             DEFAULT 'Y' NOT NULL,
	rdate	    DATE	            NOT	NULL,
	content	    VARCHAR(100)	    NOT	NULL
);

COMMENT ON TABLE learningdata is '학습 데이터';
COMMENT ON COLUMN learningdata.datano is '학습 데이터 번호';
COMMENT ON COLUMN learningdata.data_type is '데이터 타입';
COMMENT ON COLUMN learningdata.ethical is '도덕성 검증 여부';
COMMENT ON COLUMN learningdata.rdate is '데이터 등록일';
COMMENT ON COLUMN learningdata.content is '학습 데이터 내용';

DROP SEQUENCE learningdata_seq;

CREATE SEQUENCE learningdata_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지