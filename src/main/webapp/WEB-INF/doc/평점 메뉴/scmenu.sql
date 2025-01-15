DROP TABLE scmenu;
DROP TABLE scmenu CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE scmenu (
	menuno	        NUMBER(10)		    NOT NULL,
	name	        VARCHAR(20)		    NOT NULL,
	explan	        VARCHAR(100)		NOT NULL,
    rdate           VARCHAR(50)        NOT NULL
);

COMMENT ON TABLE scmenu is '평점';
COMMENT ON COLUMN scmenu.menuno is '평점 번호';
COMMENT ON COLUMN scmenu.name is '평점';
COMMENT ON COLUMN scmenu.explan is '설명';
COMMENT ON COLUMN scmenu.explan is '등록일';

DROP SEQUENCE scmenu_seq;

CREATE SEQUENCE scmenu_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
SELECT * FROM scmenu;

INSERT INTO scmenu(menuno, name, explan, rdate)
VALUES(scmenu_seq.nextval, '일기', '일기에 대한 평점', sysdate);

INSERT INTO scmenu(menuno, name, explan, rdate)
VALUES(scmenu_seq.nextval, '달력', '달력에 대한 평점', sysdate);

INSERT INTO scmenu(menuno, name, explan, rdate)
VALUES(scmenu_seq.nextval, '채팅', '채팅에 대한 평점', sysdate);

INSERT INTO scmenu(menuno, name, explan, rdate)
VALUES(scmenu_seq.nextval, '설문조사', '설문조사에 대한 평점', sysdate);

INSERT INTO scmenu(menuno, name, explan, rdate)
VALUES(scmenu_seq.nextval, '그림', '그림에 대한 평점', sysdate);

INSERT INTO scmenu(menuno, name, explan, rdate)
VALUES(scmenu_seq.nextval, '로그인', '로그인에 대한 평점', sysdate);

INSERT INTO scmenu(menuno, name, explan, rdate)
VALUES(scmenu_seq.nextval, '공지사항', '공지사항에 대한 평점', sysdate);

INSERT INTO scmenu(menuno, name, explan, rdate)
VALUES(scmenu_seq.nextval, '감정', '감정에 대한 평점', sysdate);

INSERT INTO scmenu(menuno, name, explan, rdate)
VALUES(scmenu_seq.nextval, '날씨', '날씨에 대한 평점', sysdate);


COMMIT;