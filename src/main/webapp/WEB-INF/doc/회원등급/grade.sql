--CREATE TABLE grade (
--    gradeno         NUMBER(5)       NOT NULL    PRIMARY KEY,   -- 등급 번호 
--    grade_name      VARCHAR2(20)    NOT NULL,                  -- 등급 이름
--    gdescription    VARCHAR2(255)   NULL,                      -- 등급 설명
--    min_points      NUMBER(10)      NOT NULL,                  -- 최소 포인트
--    max_points      NUMBER(10)      NOT NULL,                  -- 최대 포인트
--    rdate           DATE            NOT NULL,                  -- 생성 날짜
--    file1           VARCHAR(200)    NULL,                      -- 원본 파일 이름
--    file1saved      VARCHAR2(100)   NULL,                      -- 서버에 저장된 파일 이름
--    thumb1          VARCHAR2(100)   NULL,                      -- 썸네일 이미지 파일 이름
--    size1           NUMBER(10)      NULL                       -- 파일 크기
--);

--COMMENT ON TABLE grade is '회원등급 테이블';
--COMMENT ON COLUMN grade.gradeno is '회원 등급 번호';
--COMMENT ON COLUMN grade.grade_name is '회원 등급 이름';
--COMMENT ON COLUMN grade.gdescription is '회원 등급 설명';
--COMMENT ON COLUMN grade.min_points is '최소 포인트';
--COMMENT ON COLUMN grade.max_points is '최대 포인트';
--COMMENT ON COLUMN grade.rdate is '생성 날짜';
--COMMENT ON COLUMN grade.FILE1 is '원본 파일 이름';
--COMMENT ON COLUMN grade.FILE1SAVED is '서버에 저장된 파일 이름';
--COMMENT ON COLUMN grade.THUMB1 is '썸네일 이미지 파일 이름';
--COMMENT ON COLUMN grade.SIZE1 is '파일 크기';

DROP TABLE grade;

CREATE TABLE grade (
    gradeno         NUMBER(5)       NOT NULL    PRIMARY KEY,   -- 등급 번호 
    grade_name      VARCHAR2(20)    NOT NULL,                  -- 등급 이름
    gdescription    VARCHAR2(255)   NULL,                      -- 등급 설명
    rdate           DATE            NOT NULL,                  -- 생성 날짜
    file1           VARCHAR(200)    NULL,                      -- 원본 파일 이름
    file1saved      VARCHAR2(100)   NULL,                      -- 서버에 저장된 파일 이름
    thumb1          VARCHAR2(100)   NULL,                      -- 썸네일 이미지 파일 이름
    size1           NUMBER(10)      NULL                       -- 파일 크기
);s

COMMENT ON TABLE grade is '회원등급 테이블';
COMMENT ON COLUMN grade.gradeno is '회원 등급 번호';
COMMENT ON COLUMN grade.grade_name is '회원 등급 이름';
COMMENT ON COLUMN grade.gdescription is '회원 등급 설명';
COMMENT ON COLUMN grade.rdate is '생성 날짜';
COMMENT ON COLUMN grade.FILE1 is '원본 파일 이름';
COMMENT ON COLUMN grade.FILE1SAVED is '서버에 저장된 파일 이름';
COMMENT ON COLUMN grade.THUMB1 is '썸네일 이미지 파일 이름';
COMMENT ON COLUMN grade.SIZE1 is '파일 크기';

commit;

DROP SEQUENCE grade_seq;

CREATE SEQUENCE grade_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999 --> NUMBER(7) 대응
  CACHE 2                       -- 2번은 메모리에서만 계산
  NOCYCLE;                     -- 다시 1부터 생성되는 것을 방지
  
SELECT gradeno, grade_name, gdescription, rdate, file1, file1saved, thumb1, size1
FROM grade
ORDER BY gradeno ASC, grade_name ASC;
