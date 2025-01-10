DROP TABLE modeltraining;
DROP TABLE modeltraining CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE modeltraining (
	trainingno	    NUMBER(10)	    NOT NULL,
	name	        VARCHAR(100)	NOT NULL,
	status	        VARCHAR(100)	NOT NULL,
	accuracy	    VARCHAR(100)	NOT NULL,
	rdate	        DATE	        NOT NULL,
	notes	        VARCHAR(100)	NOT NULL,
	st_time	        DATE	            NOT NULL,
	end_time	    DATE	        NULL,
	memberno	    NUMBER(10)	    NOT NULL
);

COMMENT ON TABLE modeltraining is '모델 학습 이력';
COMMENT ON COLUMN modeltraining.trainingno is '모델 번호';
COMMENT ON COLUMN modeltraining.name is '모델 이름';
COMMENT ON COLUMN modeltraining.status is '학습 상태';
COMMENT ON COLUMN modeltraining.accuracy is '학습 정확도';
COMMENT ON COLUMN modeltraining.rdate is '학습 날짜';
COMMENT ON COLUMN modeltraining.notes is '학습 관련 메모';
COMMENT ON COLUMN modeltraining.st_time is '시작 시간';
COMMENT ON COLUMN modeltraining.end_time is '종료 시간';
COMMENT ON COLUMN modeltraining.memberno is '등록한 회원 번호';


DROP SEQUENCE modeltraining_seq;

CREATE SEQUENCE modeltraining_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
SELECT * FROM modeltraining;

INSERT INTO modeltraining(trainingno, name, status, accuracy, rdate, notes, st_time, memberno)
VALUES(modeltraining_seq.nextval,'child_edu_v1', '학습 중', '90%',sysdate, '놀이공원 간 아이 학습 내용', sysdate, 1);

COMMIT;