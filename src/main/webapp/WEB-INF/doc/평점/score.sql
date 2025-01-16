DROP TABLE score;
DROP TABLE score CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE score (
    scoreno                 NUMBER(10)  PRIMARY KEY NOT NULL,
    diary_score             NUMBER(1)   NOT NULL,
    calendar_score          NUMBER(1)   NOT NULL,
    chat_score              NUMBER(1)   NOT NULL,
    login_score             NUMBER(1)   NOT NULL,
    survey_score            NUMBER(1)   NOT NULL,
    drawing_score           NUMBER(1)   NOT NULL,
    emotion_score           NUMBER(1)   NOT NULL,
    weather_score           NUMBER(1)   NOT NULL,
    notice_score            NUMBER(1)   NOT NULL,
    word_score              NUMBER(1)   NOT NULL,
    total                   NUMBER(1)   NULL,
    memberno                NUMBER(10)  NOT NULL,
    rdate                   VARCHAR(10) NOT NULL
);


COMMENT ON TABLE score IS '평점';
COMMENT ON COLUMN score.scoreno IS '평점 번호';
COMMENT ON COLUMN score.diary_score IS '일기 점수';
COMMENT ON COLUMN score.calendar_score IS '달력 점수';
COMMENT ON COLUMN score.chat_score IS '채팅 점수';
COMMENT ON COLUMN score.login_score IS '로그인 점수';
COMMENT ON COLUMN score.survey_score IS '설문조사 점수';
COMMENT ON COLUMN score.drawing_score IS '그림 점수';
COMMENT ON COLUMN score.emotion_score IS '감정 점수';
COMMENT ON COLUMN score.weather_score IS '날씨 점수';
COMMENT ON COLUMN score.notice_score IS '공지사항 점수';
COMMENT ON COLUMN score.word_score IS '금지단어 점수';
COMMENT ON COLUMN score.total IS '총점수';
COMMENT ON COLUMN score.memberno IS '회원 번호';
COMMENT ON COLUMN score.rdate IS '등록일';


DROP SEQUENCE score_seq;

CREATE SEQUENCE score_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
SELECT * FROM score;

COMMIT;