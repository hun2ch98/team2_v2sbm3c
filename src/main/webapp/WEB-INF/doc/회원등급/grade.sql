DROP TABLE grade;
DROP TABLE grade CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE grade (
    gradeno         NUMBER(5)       NOT NULL    PRIMARY KEY,   -- 등급 번호 
    grade_name      VARCHAR2(20)    NOT NULL,                  -- 등급 이름
    evo_criteria    VARCHAR2(255)   NOT NULL,                  -- 진화 기준
    rdate           DATE            NOT NULL,                  -- 생성 날짜
    evolution       VARCHAR2(200)   NOT NULL,                  -- 진화 과정
    file1           VARCHAR(200)    NULL,
    file1saved      VARCHAR2(100)   NULL,
	thumb1          VARCHAR2(100)	NULL,
	size1           NUMBER(10)		NULL
);

COMMENT ON TABLE GRADE is '회원등급 테이블';
COMMENT ON COLUMN GRADE.GRADENO is '회원 등급 번호';
COMMENT ON COLUMN GRADE.GRADE_NAME is '회원 등급 이름';
COMMENT ON COLUMN GRADE.EVO_CRITERIA is '진화 기준 설명';
COMMENT ON COLUMN GRADE.RDATE is '생성 날짜';
COMMENT ON COLUMN GRADE.EVOLUTION is '진화 과정';
COMMENT ON COLUMN GRADE.FILE1 is '파일 업로드';
COMMENT ON COLUMN GRADE.FILE1SAVED is '실제 저장된 메인 이미지';
COMMENT ON COLUMN GRADE.THUMB1 is '메인 이미지 Preview';
COMMENT ON COLUMN GRADE.SIZE1 is '메인 이미지 크기';

commit;

DROP SEQUENCE grade_seq;

CREATE SEQUENCE grade_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999 --> NUMBER(7) 대응
  CACHE 2                       -- 2번은 메모리에서만 계산
  NOCYCLE;                     -- 다시 1부터 생성되는 것을 방지
  
SELECT * FROM grade;
  
SELECT gradeno, grade_name, evo_criteria, rdate, evolution
FROM grade
ORDER BY gradeno ASC, grade_name ASC;

ALTER TABLE grade MODIFY grade_name VARCHAR2(50);


