DROP TABLE modeltraining;
DROP TABLE modeltraining CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE modeltraining (
	trainingno	    NUMBER(10)	    NOT NULL,
	name	        VARCHAR(100)	NOT NULL,
	status	        VARCHAR(100)	NOT NULL,
	accuracy	    NUMBER(5, 2)	NOT NULL,
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
VALUES(modeltraining_seq.nextval,'child_edu_v1', '학습 중', '90',sysdate, '놀이공원 간 아이 학습 내용', sysdate, 1);

INSERT INTO modeltraining (trainingno, name, status, accuracy, rdate, notes, st_time, end_time, memberno)
VALUES (modeltraining_seq.NEXTVAL, 'child_edu_v1', '학습 중', 90.00, TO_DATE('2025-01-17', 'YYYY-MM-DD'), '놀이공원 간 아이 학습 내용', TO_DATE('2025-01-17 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-01-17 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);

INSERT INTO modeltraining (trainingno, name, status, accuracy, rdate, notes, st_time, end_time, memberno)
VALUES (modeltraining_seq.NEXTVAL, 'adult_edu_v2', '학습 완료', 85.50, TO_DATE('2025-01-16', 'YYYY-MM-DD'), '성인 교육 프로그램 분석', TO_DATE('2025-01-16 09:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-01-16 11:30:00', 'YYYY-MM-DD HH24:MI:SS'), 2);

INSERT INTO modeltraining (trainingno, name, status, accuracy, rdate, notes, st_time, end_time, memberno)
VALUES (modeltraining_seq.NEXTVAL, 'finance_model_v1', '학습 중', 92.00, TO_DATE('2025-01-15', 'YYYY-MM-DD'), '재무 모델 학습 및 평가', TO_DATE('2025-01-15 08:45:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-01-15 10:45:00', 'YYYY-MM-DD HH24:MI:SS'), 3);

INSERT INTO modeltraining (trainingno, name, status, accuracy, rdate, notes, st_time, end_time, memberno)
VALUES (modeltraining_seq.NEXTVAL, 'data_analysis_v2', '학습 완료', 88.75, TO_DATE('2025-01-14', 'YYYY-MM-DD'), '데이터 분석 실습', TO_DATE('2025-01-14 13:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-01-14 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), 4);

INSERT INTO modeltraining (trainingno, name, status, accuracy, rdate, notes, st_time, end_time, memberno)
VALUES (modeltraining_seq.NEXTVAL, 'health_model_v1', '학습 중', 94.00, TO_DATE('2025-01-13', 'YYYY-MM-DD'), '건강 모델 개발', TO_DATE('2025-01-13 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-01-13 16:30:00', 'YYYY-MM-DD HH24:MI:SS'), 5);

INSERT INTO modeltraining (trainingno, name, status, accuracy, rdate, notes, st_time, end_time, memberno)
VALUES (modeltraining_seq.NEXTVAL, 'marketing_model_v1', '학습 중', 89.25, TO_DATE('2025-01-12', 'YYYY-MM-DD'), '마케팅 모델 분석', TO_DATE('2025-01-12 11:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-01-12 13:00:00', 'YYYY-MM-DD HH24:MI:SS'), 6);

INSERT INTO modeltraining (trainingno, name, status, accuracy, rdate, notes, st_time, end_time, memberno)
VALUES (modeltraining_seq.NEXTVAL, 'ml_model_v1', '학습 완료', 93.50, TO_DATE('2025-01-11', 'YYYY-MM-DD'), '머신러닝 모델 학습', TO_DATE('2025-01-11 07:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-01-11 09:30:00', 'YYYY-MM-DD HH24:MI:SS'), 7);

INSERT INTO modeltraining (trainingno, name, status, accuracy, rdate, notes, st_time, end_time, memberno)
VALUES (modeltraining_seq.NEXTVAL, 'ai_model_v1', '학습 중', 91.00, TO_DATE('2025-01-10', 'YYYY-MM-DD'), '인공지능 모델 개발', TO_DATE('2025-01-10 16:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-01-10 18:00:00', 'YYYY-MM-DD HH24:MI:SS'), 8);

INSERT INTO modeltraining (trainingno, name, status, accuracy, rdate, notes, st_time, end_time, memberno)
VALUES (modeltraining_seq.NEXTVAL, 'vision_model_v1', '학습 완료', 87.50, TO_DATE('2025-01-09', 'YYYY-MM-DD'), '비전 모델 학습', TO_DATE('2025-01-09 10:15:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-01-09 12:15:00', 'YYYY-MM-DD HH24:MI:SS'), 9);

INSERT INTO modeltraining (trainingno, name, status, accuracy, rdate, notes, st_time, end_time, memberno)
VALUES (modeltraining_seq.NEXTVAL, 'robot_model_v1', '학습 중', 90.50, TO_DATE('2025-01-08', 'YYYY-MM-DD'), '로봇 모델 개발', TO_DATE('2025-01-08 17:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-01-08 19:30:00', 'YYYY-MM-DD HH24:MI:SS'), 10);

COMMIT;