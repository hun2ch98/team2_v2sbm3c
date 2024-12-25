DROP TABLE grade;

--CREATE TABLE grade (
--    gradeno NUMBER(5) PRIMARY KEY,       -- 등급 번호 
--    memberno NUMBER(5),                  -- 회원 번호
--    diaryno NUMBER(5),                   -- 일기 번호
--    grade_name VARCHAR2(20),             -- 등급 이름
--    grade_img VARCHAR2(200),             -- 등급별 이미지 경로
--    img_saved VARCHAR2(200),             -- 실제 이미지 저장 경로
--    img_size NUMBER(10),                 -- 이미지 크기 (KB 단위)
--    gdescription VARCHAR2(255),          -- 등급 설명
--    min_points NUMBER(10),               -- 최소 포인트
--    max_points NUMBER(10),               -- 최대 포인트
--    created_at DATE DEFAULT SYSDATE,     -- 생성 날짜
--    updated_at DATE,                      -- 수정 날짜
--    FOREIGN KEY (memberno) REFERENCES member(memberno), -- 회원 테이블의 외래 키
--    FOREIGN KEY (diaryno) REFERENCES diary(diaryno)      -- 일기 테이블의 외래 키
--);

CREATE TABLE grade (
    gradeno         NUMBER(5)       NOT NULL    PRIMARY KEY,   -- 등급 번호 
    memberno        NUMBER(5)       NULL,                      -- 회원 번호
    grade_name      VARCHAR2(20)    NOT NULL,                  -- 등급 이름
    gdescription    VARCHAR2(255)   NULL,                      -- 등급 설명
    min_points      NUMBER(10)      NOT NULL,                  -- 최소 포인트
    max_points      NUMBER(10)      NOT NULL,                  -- 최대 포인트
    rdate           DATE            NOT NULL,                  -- 생성 날짜
    img_url         VARCHAR2(255)   NULL,                      -- 이미지 경로
    FOREIGN KEY (memberno) REFERENCES member(memberno)
);

COMMENT ON TABLE grade is '회원등급 테이블';
COMMENT ON COLUMN grade.gradeno is '회원 등급 번호';
COMMENT ON COLUMN grade.memberno is '회원 번호';
COMMENT ON COLUMN grade.grade_name is '회원 등급 이름';
COMMENT ON COLUMN grade.gdescription is '회원 등급 설명';
COMMENT ON COLUMN grade.min_points is '최소 포인트';
COMMENT ON COLUMN grade.max_points is '최대 포인트';
COMMENT ON COLUMN grade.rdate is '생성 날짜';
COMMENT ON COLUMN grade.img_url is '등급 이미지 경로';

commit;

--COMMENT ON TABLE grade IS '회원등급 테이블';
--COMMENT ON COLUMN grade.gradeno IS '회원 등급 번호';
--COMMENT ON COLUMN grade.memberno IS '회원 번호';  -- 회원 테이블의 외래 키
--COMMENT ON COLUMN grade.diaryno IS '일기 번호';    -- 일기 테이블의 외래 키
--COMMENT ON COLUMN grade.grade_name IS '회원 등급 이름';
--COMMENT ON COLUMN grade.grade_img IS '회원 등급별 이미지 경로';
--COMMENT ON COLUMN grade.img_saved IS '회원 등급 실제 저장된 이미지 경로';
--COMMENT ON COLUMN grade.img_size IS '회원 등급 이미지 파일 크기 (KB 단위)';
--COMMENT ON COLUMN grade.gdescription IS '회원 등급 설명';
--COMMENT ON COLUMN grade.min_points IS '회원 등급에 필요한 최소 포인트';
--COMMENT ON COLUMN grade.max_points IS '회원 등급의 최대 포인트 범위';
--COMMENT ON COLUMN grade.created_at IS '회원 등급 생성 날짜';
--COMMENT ON COLUMN grade.updated_at IS '회원 등급 마지막 수정 날짜';


DROP SEQUENCE grade_seq;

CREATE SEQUENCE grade_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999 --> NUMBER(7) 대응
  CACHE 2                       -- 2번은 메모리에서만 계산
  NOCYCLE;                     -- 다시 1부터 생성되는 것을 방지
