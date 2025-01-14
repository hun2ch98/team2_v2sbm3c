-- GPT ChatBot용 관리자 계정 추가
INSERT INTO member(memberno, gradeno, id, passwd, email, name, birth, mdate, grade)
VALUES (member_seq.nextval, 10, 'gpt', '1234', 'gpt@example.com', 'Chatting 관리자', '1990-01-01', sysdate, 1);


COMMIT;

-- 테이블 삭제
DROP TABLE chatting;

-- 테이블 생성
CREATE TABLE chatting(
  chattingno   NUMBER(8)    NOT NULL PRIMARY KEY,
  memberno     NUMBER(10)   NOT NULL, -- 회원 번호, 레코드를 구분하는 컬럼
  msg          VARCHAR(300) NOT NULL, -- 채팅 메시지
  rdate        DATE             NOT NULL, -- 가입일
  FOREIGN KEY (memberno) REFERENCES member (memberno)
);

COMMENT ON TABLE CHATTING is '회원';
COMMENT ON COLUMN CHATTING.CHATTINGNO is '채팅 번호';
COMMENT ON COLUMN CHATTING.MEMBERNO is '회원 번호';
COMMENT ON COLUMN CHATTING.MSG is '채팅 메시지';
COMMENT ON COLUMN CHATTING.RDATE is '등록일';

DROP SEQUENCE chatting_seq;

CREATE SEQUENCE chatting_seq
  START WITH 1        -- 시작 번호
  INCREMENT BY 1      -- 증가값
  MAXVALUE 99999999   -- 최대값: 99999999 --> NUMBER(8) 대응
  CACHE 2             -- 2번은 메모리에서만 계산
  NOCYCLE;            -- 다시 1부터 생성되는 것을 방지

INSERT INTO chatting(chattingno, memberno, msg, rdate)
VALUES(chatting_seq.nextval, 3, '안녕하세요.',sysdate);

INSERT INTO chatting(chattingno, memberno, msg, rdate)
VALUES(chatting_seq.nextval, 1, '네 안녕하세요, 저는 챗봇입니다.',sysdate);

COMMIT;

SELECT chattingno, memberno, msg, rdate FROM chatting ORDER BY chattingno DESC;

CHATTINGNO   MEMBERNO MSG                              RDATE              
---------- ---------- ------------------------------ -------------------
         2          1 네 안녕하세요, 저는 챗봇입니다.      2023-11-23 04:02:03
         1          3 안녕하세요.                       2023-11-23 04:02:03

-- 조회
SELECT chattingno, memberno, msg, rdate
FROM chatting
WHERE chattingno=1;

-- 2023-11-23일자 채팅만 출력
SELECT rdate, TO_CHAR(rdate), SUBSTR(rdate, 1, 10), SUBSTR(TO_CHAR(rdate), 1, 10)
FROM chatting
WHERE SUBSTR(TO_CHAR(rdate), 1, 10) = '2023-11-23';

RDATE                TO_CHAR(RDATE)       SUBSTR(RDA  SUBSTR(TO_
-------------------  -------------------  ----------  ----------
2023-11-23 04:58:42  2023-11-23 04:58:42  2023-11-23  2023-11-23
2023-11-23 04:58:42  2023-11-23 04:58:42  2023-11-23  2023-11-23

SELECT chattingno, memberno, msg, rdate
FROM chatting
WHERE SUBSTR(rdate, 1, 10) = '2023-11-23';

SELECT chattingno, memberno, msg, rdate
FROM chatting
WHERE TO_CHAR(rdate, 'YYYY-MM-DD') = '2023-11-23';
  
SELECT chattingno, memberno, msg, rdate
FROM chatting
WHERE memberno=1 and SUBSTR(rdate, 1, 10) = '2023-11-23';

-- 시분초 일치하지 않음, 조회안됨 X
SELECT chattingno, memberno, msg, rdate
FROM chatting
WHERE memberno=1 and rdate = TO_DATE('2023-11-23', 'YYYY-MM-DD');

-- 문열로 변경하는 가능함
SELECT chattingno, memberno, msg, rdate
FROM chatting
WHERE memberno=1 and TO_CHAR(rdate, 'YYYY-MM-DD') = '2023-11-23';

-- LIKE 조회
SELECT chattingno, memberno, msg, rdate
FROM chatting
WHERE memberno=1 and msg LIKE '%안녕%';


UPDATE chatting
SET msg='반가워요~'
WHERE chattingno=2;

COMMIT;

SELECT chattingno, memberno, msg, rdate FROM chatting ORDER BY chattingno DESC;

DELETE FROM chatting
WHERE chattingno=1;

DELETE FROM chatting;

SELECT chattingno, memberno, msg, rdate FROM chatting ORDER BY chattingno DESC;

COMMIT;


