/**********************************/
/* Table Name: 설문조사 */
/**********************************/
-- 삭제
DROP TABLE survey;

CREATE TABLE survey (
	surveyno	        NUMBER(10)		NOT NULL,
	topic	            VARCHAR(100)		NOT NULL,
	start_date	        VARCHAR(10)		NULL,
	end_date	        VARCHAR(10)		NULL,
	participants_cnt	NUMBER(7)		NOT NULL,
	is_continue	        CHAR(1)		NOT NULL,
	poster	            VARCHAR(100)		NULL,
	poster_saved	    VARCHAR(100)		NULL,
	poster_size	        NUMBER(10)		NULL,
	poster_thumb	    VARCHAR(100)		NULL
);

COMMENT ON TABLE survey is '설문조사';
COMMENT ON COLUMN survey.surveyno is '설문 조사 번호';
COMMENT ON COLUMN survey.topic is '제목';
COMMENT ON COLUMN survey.startdate is '시작 날짜';
COMMENT ON COLUMN survey.enddate is '종료 날짜';
COMMENT ON COLUMN survey.poster is '포스터 파일';
COMMENT ON COLUMN survey.postersaved is '포스터 저장된 파일명';
COMMENT ON COLUMN survey.posterthumb is '포스터 썸네일';
COMMENT ON COLUMN survey.postersize is '포스터 이미지 크기';
COMMENT ON COLUMN survey.cnt is '참여 인원';
COMMENT ON COLUMN survey.continueyn is '진행 여부';