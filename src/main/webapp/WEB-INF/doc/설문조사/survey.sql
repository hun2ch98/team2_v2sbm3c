/**********************************/
/* Table Name: 설문조사 */
/**********************************/
-- 삭제
DROP TABLE survey;

CREATE TABLE survey (
	surveyno	    NUMBER(10)		NOT NULL,
    memberno        NUMBER(10)      NOT NULL,
	topic	        VARCHAR(100)	NOT NULL,
	sdate	        VARCHAR(10)		    NULL,
	edate	        VARCHAR(10)		    NULL,
	s_number	    NUMBER(7)		NOT NULL,
	is_continue	    VARCHAR(100)	NOT NULL,
	poster	        VARCHAR(100)		NULL,
	poster_saved	VARCHAR(100)		NULL,
	poster_size	    NUMBER(10)		    NULL,
	poster_thumb	VARCHAR(100)		NULL,
    FOREIGN KEY (memberno)  REFERENCES member (memberno)
);

COMMENT ON TABLE SURVEY is '설문조사';
COMMENT ON COLUMN SURVEY.SURVEYNO is '설문 조사 번호';
COMMENT ON COLUMN BOARD.MEMBERNO is '회원 번호';
COMMENT ON COLUMN SURVEY.TOPIC is '제목';
COMMENT ON COLUMN SURVEY.SDATE is '시작 날짜';
COMMENT ON COLUMN SURVEY.EDATE is '종료 날짜';
COMMENT ON COLUMN SURVEY.POSTER is '포스터 파일';
COMMENT ON COLUMN SURVEY.POSTER_SAVED is '포스터 저장된 파일명';
COMMENT ON COLUMN SURVEY.POSTER_THUMB is '포스터 썸네일';
COMMENT ON COLUMN SURVEY.POSTER_SIZE is '포스터 이미지 크기';
COMMENT ON COLUMN SURVEY.S_NUMBER is '참여 인원';
COMMENT ON COLUMN SURVEY.IS_CONTINUE is '진행 여부';

DROP SEQUENCE survey_seq;

CREATE SEQUENCE survey_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;                  -- 다시 1부터 생성되는 것을 방지

COMMIT;

-- create
INSERT INTO survey(surveyno, topic, sdate, edate, s_number, is_continue, poster, poster_saved, poster_thumb, poster_size)
VALUES (survey_seq.nextval, 'test', '2024/12/23', '2025/01/01', 5, 'Y', 'test1.jpg', 'test1_1.jpg', 'test1_t.jpg', 1000);

-- read
SELECT * FROM survey;
SURVEYNO TOPIC                                                                                                SDATE      EDATE        S_NUMBER I POSTER                                                                                               POSTER_SAVED                                                                                         POSTER_SIZE POSTER_THUMB                                                                                        
---------- ---------------------------------------------------------------------------------------------------- ---------- ---------- ---------- - ---------------------------------------------------------------------------------------------------- ---------------------------------------------------------------------------------------------------- ----------- ----------------------------------------------------------------------------------------------------
         1 test                                                                                                 2024/12/23 2025/01/01          5 Y test1.jpg                                                                                            test1_1.jpg                                                                                                 1000 test1_t.jpg                                                                                         

-- update
UPDATE survey SET topic = 'test2' WHERE surveyno = 1;
 SURVEYNO TOPIC                                                                                                SDATE      EDATE        S_NUMBER I POSTER                                                                                               POSTER_SAVED                                                                                         POSTER_SIZE POSTER_THUMB                                                                                        
---------- ---------------------------------------------------------------------------------------------------- ---------- ---------- ---------- - ---------------------------------------------------------------------------------------------------- ---------------------------------------------------------------------------------------------------- ----------- ----------------------------------------------------------------------------------------------------
         1 test2                                                                                                2024/12/23 2025/01/01          5 Y test1.jpg                                                                                            test1_1.jpg                                                                                                 1000 test1_t.jpg                                                                                         

-- delete
DELETE FROM survey;

COMMIT;