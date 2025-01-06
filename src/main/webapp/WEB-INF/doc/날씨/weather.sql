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
  
  
INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '맑음', '날씨가 매우 화창하고 맑음.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '구름 조금', '구름이 약간 있지만 전반적으로 맑음.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '흐림', '하늘이 전체적으로 흐림.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '흐리고 바람', '흐리고 바람이 조금 강하게 부는 날씨.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '약간 비', '약간의 비가 내리는 상황.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '천둥비', '천둥과 함께 비가 내림.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '약간 흐리고 바람', '약간 흐리고 바람이 부는 날씨.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '비와 눈', '비와 눈이 섞여 내리는 상황.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '태풍', '강한 바람과 함께 태풍이 발생한 상황.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '번개', '하늘에 번개가 치는 날씨.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '맑지만 비', '맑지만 간헐적으로 비가 내리는 날씨.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '눈', '눈이 내리는 상황.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '맑지만 눈', '맑지만 간헐적으로 눈이 내리는 날씨.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '흐린 밤', '흐린 밤하늘.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '비오는 밤', '밤에 비가 내리는 상황.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '눈오는 밤', '밤에 눈이 내리는 상황.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '바람이 많이 부는 밤', '바람이 많이 부는 밤하늘.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '천둥치고 비오는 밤', '천둥이 치며 비가 내리는 밤.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '폭우', '매우 강한 폭우가 내리는 상황.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '천둥치는 폭우', '천둥과 함께 폭우가 내리는 상황.');

INSERT INTO weather(weatherno, type, explan)
VALUES(weather_seq.nextval, '무지개', '비가 그친 후 무지개가 나타난 상황.');

SELECT * FROM weather;

COMMIT;