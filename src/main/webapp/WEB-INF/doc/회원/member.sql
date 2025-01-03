DROP TABLE member;
DROP TABLE member CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE member(
    memberno        NUMBER(10)      NOT NULL,           -- 회원 번호
    gradeno         NUMBER(10)      NOT NULL,           -- 등급 번호
    id              VARCHAR(50)     NOT NULL    UNIQUE, -- 아이디, 중복 X
    passwd          VARCHAR(255)    NOT NULL,           -- 패스워드, 영숫자 조합, 암호화
    email           VARCHAR(50)     NOT NULL    UNIQUE, -- 이메일
    name            VARCHAR(30)     NOT NULL,           -- 성명, 한글 10자 저장 가능
    nickname        VARCHAR(30)         NULL,           -- 별명
    birth           VARCHAR(20)     NOT NULL,           -- 생년월일
    zipcode         VARCHAR(5)          NULL,           -- 우편번호, 12345
    address1        VARCHAR(80)         NULL,           -- 주소 1
    address2        VARCHAR(50)         NULL,           -- 주소 2
    pf_img          VARCHAR(100)        NULL,           -- 프로필 이미지
    file1saved      VARCHAR(100)        NULL,
    thumb1          VARCHAR(100)        NULL,
    size1           NUMBER(10)      DEFAULT 0 NULL,
    replycnt        NUMBER(6)       DEFAULT 0 NOT NULL, -- 작성한 댓글 수
    mdate           DATE            NOT NULL,           -- 가입일
    grade           NUMBER(2)       NOT NULL,           -- 등급(1~10: 관리자, 11~20: 회원, 40~49: 정지 회원, 99: 탈퇴 회원)
    PRIMARY KEY (memberno),
    FOREIGN KEY (gradeno)  REFERENCES grade (gradeno)
);

COMMENT ON TABLE MEMBER is '회원';
COMMENT ON COLUMN MEMBER.MEMBERNO is '회원 번호';
COMMENT ON COLUMN MEMBER.GRADENO is '등급 번호';
COMMENT ON COLUMN MEMBER.ID is '아이디';
COMMENT ON COLUMN MEMBER.PASSWD is '패스워드';
COMMENT ON COLUMN MEMBER.EMAIL is '이메일';
COMMENT ON COLUMN MEMBER.NAME is '성명';
COMMENT ON COLUMN MEMBER.NICKNAME is '닉네임';
COMMENT ON COLUMN MEMBER.BIRTH is '생년월일';
COMMENT ON COLUMN MEMBER.ZIPCODE is '우편번호';
COMMENT ON COLUMN MEMBER.ADDRESS1 is '주소1';
COMMENT ON COLUMN MEMBER.ADDRESS2 is '주소2';
COMMENT ON COLUMN MEMBER.PF_IMG is '프로필 이미지';
COMMENT ON COLUMN MEMBER.FILE1SAVED is '실제 저장된 프로필 이미지';
COMMENT ON COLUMN MEMBER.THUMB1 is '프로필 이미지 Preview';
COMMENT ON COLUMN MEMBER.SIZE1 is '프로필 이미지 크기';
COMMENT ON COLUMN MEMBER.REPLYCNT is '작성 댓글 수';
COMMENT ON COLUMN MEMBER.MDATE is '가입일';
COMMENT ON COLUMN MEMBER.GRADE is '등급';

DROP SEQUENCE member_seq;

CREATE SEQUENCE member_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999 --> NUMBER(7) 대응
  CACHE 2                       -- 2번은 메모리에서만 계산
  NOCYCLE;                     -- 다시 1부터 생성되는 것을 방지

commit;

SELECT * FROM member;

-- 모두 삭제
DELETE FROM member;


SELECT * FROM member WHERE gradeno = ?;

--1. 등록
-- 회원 관리용 계정
INSERT INTO member (memberno, id, passwd, email, name, nickname, birth, zipcode,
                    address1, address2, pf_img, mdate, grade)
VALUES (member_seq.nextval, 'admin', '1234', 'admin@mail.com', '통합 관리자',
        '운영자', TO_DATE('2024-12-18', 'YYYY-MM-DD'), '서울시 종로구', '2.png', SYSDATE, 1);

-- 개인 회원 테스트 계정
INSERT INTO member (memberno, id, passwd, email, name, nickname, birth, zipcode,
                    address1, address2, pf_img, mdate, grade)
VALUES (member_seq.nextval, 'member', '1234', 'member@mail.com', '일반 회원',
        '일반인', TO_DATE('2024-12-18', 'YYYY-MM-DD'), '서울시 종로2가', '1.png', SYSDATE, 10);

--2. 목록
SELECT memberno, id, passwd, email, name, nickname, birth, zipcode, address1, address2, pf_img, file1saved, thumb1, size1, mdate, grade, gradeno
FROM member
ORDER BY grade ASC, id ASC;

--특정 회원 삭제
DELETE FROM member
WHERE memberno=1;

-- grade 등급 수정
UPDATE member SET GRADE = 1 WHERE ID = 'admin';

commit;

SELECT id, COUNT(*)
FROM member
GROUP BY id
HAVING COUNT(*) > 1;

-- 예시: 중복된 id 제거
DELETE FROM member
WHERE id = 'admin' AND memberno NOT IN (
    SELECT MIN(memberno)
    FROM member
    WHERE id = 'admin'
);