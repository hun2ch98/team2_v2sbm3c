DROP TABLE member;
DROP TABLE member CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE member(
    memberno        NUMBER(10)      NOT NULL    PRIMARY KEY,
    id              NUMBER(10)      NOT NULL,
    passwd          VARCHAR(255)    NOT NULL,
    email           VARCHAR(50)     NOT NULL,
    name            VARCHAR(30)     NOT NULL,
    nickname        VARCHAR(30)     NULL,
    bitrth          VARCHAR(20)     NOT NULL,
    address         VARCHAR(100)    NULL,
    role            NUMBER(1)       NOT NULL,
    pf_img          VARCHAR(255)    NULL
);

COMMENT ON TABLE MEMBER is '회원';
COMMENT ON COLUMN MEMBER.MEMBERNO is '회원 번호';
COMMENT ON COLUMN MEMBER.ID is '아이디';
COMMENT ON COLUMN MEMBER.PASSWD is '패스워드';
COMMENT ON COLUMN MEMBER.EMAIL is '이메일';
COMMENT ON COLUMN MEMBER.NAME is '성명';
COMMENT ON COLUMN MEMBER.NICKNAME is '닉네임';
COMMENT ON COLUMN MEMBER.BITRTH is '생년월일';
COMMENT ON COLUMN MEMBER.ADDRESS is '주소';
COMMENT ON COLUMN MEMBER.ROLE is '규칙';
COMMENT ON COLUMN MEMBER.PF_IMG is '프로필 이미지';

commit;

DROP SEQUENCE member_seq;

CREATE SEQUENCE member_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999 --> NUMBER(7) 대응
  CACHE 2                       -- 2번은 메모리에서만 계산
  NOCYCLE;                     -- 다시 1부터 생성되는 것을 방지

--1. 등록







