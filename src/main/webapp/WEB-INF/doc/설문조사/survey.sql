/**********************************/
/* Table Name: 설문조사 */
/**********************************/

-- 삭제
DROP TABLE survey;

CREATE TABLE survey (
	surveyno	    NUMBER(10)		NOT NULL    PRIMARY KEY,
    memberno        NUMBER(10)      NOT NULL,
	topic	        VARCHAR(100)	NOT NULL,
	sdate	        VARCHAR(10)		NOT NULL,
	edate	        VARCHAR(10)		NOT NULL,
	s_number	    NUMBER(7)		NOT NULL,
	is_continue	    VARCHAR(100)	NOT NULL,
    file1           VARCHAR(200)        NULL,
    file1saved      VARCHAR2(100)		 NULL,
	thumb1          VARCHAR2(100)		 NULL,
	size1           NUMBER(10)		     NULL,
    goodcnt         NUMBER(7)       NOT  NULL,
    FOREIGN KEY (memberno)  REFERENCES member (memberno)
);
ALTER TABLE survey ADD GOODCNT NUMBER(7) DEFAULT 0;
ALTER TABLE survey MODIFY (goodcnt NUMBER(7) NOT NULL);


DESC SURVEY;
-- ALTER TABLE survey MODIFY goodcnt NUMBER(7);


COMMENT ON TABLE SURVEY is '설문조사';
COMMENT ON COLUMN SURVEY.SURVEYNO is '설문 조사 번호';
COMMENT ON COLUMN BOARD.MEMBERNO is '회원 번호';
COMMENT ON COLUMN SURVEY.TOPIC is '제목';
COMMENT ON COLUMN SURVEY.SDATE is '시작 날짜';
COMMENT ON COLUMN SURVEY.EDATE is '종료 날짜';
COMMENT ON COLUMN SURVEY.FILE1 is '파일 업로드';
COMMENT ON COLUMN SURVEY.FILE1SAVED is '실제 저장된 메인 이미지';
COMMENT ON COLUMN SURVEY.THUMB1 is '메인 이미지 Preview';
COMMENT ON COLUMN SURVEY.SIZE1 is '메인 이미지 크기';
COMMENT ON COLUMN SURVEY.S_NUMBER is '참여 인원';
COMMENT ON COLUMN SURVEY.IS_CONTINUE is '진행 여부';
COMMENT ON COLUMN SURVEY.GOODCNT is '추천';

DROP SEQUENCE survey_seq;
-- ALTER TABLE survey MODIFY visible CHAR(1);
CREATE SEQUENCE survey_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;                  -- 다시 1부터 생성되는 것을 방지

COMMIT;

-- create
INSERT INTO survey(surveyno, memberno, topic, sdate, edate, s_number, is_continue)
VALUES (survey_seq.nextval, 1, '교육적 가치', '2024/12/23', '2025/01/01', 5, 'Y');
INSERT INTO survey(surveyno, memberno, topic, sdate, edate, s_number, is_continue)
VALUES (survey_seq.nextval, 1, '언어와 표현', '2024/12/23', '2025/01/01', 5, 'Y');
INSERT INTO survey(surveyno, memberno, topic, sdate, edate, s_number, is_continue)
VALUES (survey_seq.nextval, 1, '다양한 콘텐츠', '2024/12/23', '2025/01/01', 5, 'Y');
INSERT INTO survey(surveyno, memberno, topic, sdate, edate, s_number, is_continue)
VALUES (survey_seq.nextval, 1, '에러 및 장애', '2024/12/23', '2025/01/01', 5, 'Y');
INSERT INTO survey(surveyno, memberno, topic, sdate, edate, s_number, is_continue)
VALUES (survey_seq.nextval, 1, 'AI 정확성', '2024/12/23', '2025/01/01', 5, 'Y');
INSERT INTO survey(surveyno, memberno, topic, sdate, edate, s_number, is_continue)
VALUES (survey_seq.nextval, 1, '저장 및 관리', '2024/12/23', '2025/01/01', 5, 'Y');
INSERT INTO survey(surveyno, memberno, topic, sdate, edate, s_number, is_continue)
VALUES (survey_seq.nextval, 1, '데이터 신뢰도', '2024/12/23', '2025/01/01', 5, 'Y');
INSERT INTO survey(surveyno, memberno, topic, sdate, edate, s_number, is_continue)
VALUES (survey_seq.nextval, 1, 'UI/UX 편리성', '2024/12/23', '2025/01/01', 5, 'Y');
INSERT INTO survey(surveyno, memberno, topic, sdate, edate, s_number, is_continue)
VALUES (survey_seq.nextval, 1, '아이의 반응', '2024/12/23', '2025/01/01', 5, 'Y');
INSERT INTO survey(surveyno, memberno, topic, sdate, edate, s_number, is_continue)
VALUES (survey_seq.nextval, 1, '추가 기능 요청', '2024/12/23', '2025/01/01', 5, 'Y');
INSERT INTO survey(surveyno, memberno, topic, sdate, edate, s_number, is_continue)
VALUES (survey_seq.nextval, 1, '기능 만족도', '2024/12/23', '2025/01/01', 5, 'Y');
INSERT INTO survey(surveyno, memberno, topic, sdate, edate, s_number, is_continue)
VALUES (survey_seq.nextval, 1, '사용 빈도', '2024/12/23', '2025/01/01', 5, 'Y');

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

UPDATE survey SET topic = '앱이 아이들에게 긍정적인 표현을 학습하는 데 도움된다고 느끼시나요?' WHERE surveyno = 42;

-- delete
DELETE FROM survey;

DELETE FROM survey WHERE topic='교육적 가치';

COMMIT;

-- ----------------------------------------------------------------------------------------------------
-- 검색, topic 검색 목록
-- ----------------------------------------------------------------------------------------------------

-- 1) 검색
SELECT surveyno, memberno, topic, sdate, edate, s_number, is_continue, LOWER(file1) as file1, file1saved, thumb1, size1
FROM survey
WHERE is_continue = 'Y'
ORDER BY surveyno DESC;

SELECT surveyno, memberno, topic, sdate, edate, s_number, is_continue, LOWER(file1) as file1, file1saved, thumb1, size1
FROM survey
WHERE is_continue = 'N'
ORDER BY surveyno DESC;

-----------------------------------------------------
레코드 수
-----------------------------------------------------
SELECT COUNT(*)
FROM survey
WHERE is_continue='N';

  COUNT(*)
----------
         2
         
SELECT COUNT(*)
FROM survey
WHERE is_continue='Y';

 COUNT(*)
----------
         3

-- ----------------------------------------------------------------------------------------------------
-- 검색 + 페이징 + 메인 이미지
-- ----------------------------------------------------------------------------------------------------
SELECT *
FROM (
    SELECT surveyno, memberno, topic, sdate, edate, s_number, is_continue, LOWER(file1) as file1, file1saved, thumb1, size1, ROWNUM AS rnum
    FROM (
        SELECT surveyno, memberno, topic, sdate, edate, s_number, is_continue, LOWER(file1) as file1, file1saved, thumb1, size1
        FROM survey
        WHERE is_continue = 'Y'
        ORDER BY surveyno DESC
    )
    WHERE ROWNUM <= 5 -- 여기서 상위 2개까지 가져옴
)
WHERE rnum >= 1; -- 여기서 1~10번 행만 가져옴

COMMIT;


-- cateno FK 특정 그룹에 속한 레코드 모두 삭제
DELETE FROM surveyitem
WHERE surveyno=14;

SELECT COUNT(*) 
FROM surveyitem 
WHERE surveyno = 14;

-- 추천
UPDATE survey SET goodcnt=goodcnt+1 WHERE surveyno=12;



