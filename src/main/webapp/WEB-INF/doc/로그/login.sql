DROP TABLE login;

CREATE TABLE login(
  loginno       NUMBER(10) NOT NULL PRIMARY KEY,
  ip            VARCHAR2(15) NOT NULL,
  logindate     DATE NOT NULL,
  memberno      NUMBER(10) NOT NULL,
  FOREIGN KEY (memberno) REFERENCES member (memberno)
);

COMMENT ON TABLE login is '로그인 내역';
COMMENT ON COLUMN login.loginno is '로그인 번호';
COMMENT ON COLUMN login.ip is '접속 IP';
COMMENT ON COLUMN login.logindate is '로그인 날짜';
COMMENT ON COLUMN login.memberno is '회원 번호';

commit;

DROP SEQUENCE LOGIN_SEQ;

CREATE SEQUENCE LOGIN_SEQ
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;                  -- 다시 1부터 생성되는 것을 방지
