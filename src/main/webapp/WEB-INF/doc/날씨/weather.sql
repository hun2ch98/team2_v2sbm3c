DROP TABLE weather;
DROP TABLE weather CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE weather (
	weatherno	NUMBER(10)		    NOT NULL,
	type	VARCHAR(50)		    NOT NULL,
    explan  VARCHAR(500)        NOT NULL,
	file1           VARCHAR(200)        NULL,
    file1saved      VARCHAR2(100)		 NULL,
	thumb1          VARCHAR2(100)		 NULL,
	size1           NUMBER(10)		     NULL
);

COMMENT ON TABLE weather is '날씨';
COMMENT ON COLUMN weather.weatherno is '날씨 번호';
COMMENT ON COLUMN weather.type is '날씨 유형';
COMMENT ON COLUMN weather.explan is '날씨 내용';
COMMENT ON COLUMN weather.FILE1 is '파일 업로드';
COMMENT ON COLUMN weather.FILE1SAVED is '실제 저장된 메인 이미지';
COMMENT ON COLUMN weather.THUMB1 is '메인 이미지 Preview';
COMMENT ON COLUMN weather.SIZE1 is '메인 이미지 크기';

DROP SEQUENCE weather_seq;

CREATE SEQUENCE weather_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지


COMMIT;

SELECT * FROM weather;
