DROP TABLE learningdata;
DROP TABLE learningdata CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE learningdata (
	datano	    NUMBER(10)		    NOT NULL,
	ethical	    VARCHAR(1)	        NOT	NULL,
	create_at	DATE		        NOT NULL,
	ques	    VARCHAR(100)		NOT NULL,
	ans	        VARCHAR(100)		NOT NULL,
    memberno    NUMBER(10)          NOT NULL
);

COMMENT ON TABLE learningdata is '학습 데이터';
COMMENT ON COLUMN learningdata.datano is '학습 데이터 번호';
COMMENT ON COLUMN learningdata.ethical is '도덕성 검증 여부';
COMMENT ON COLUMN learningdata.create_at is '데이터 등록일';
COMMENT ON COLUMN learningdata.ques is '학습 데이터 질문';
COMMENT ON COLUMN learningdata.ans is '학습 데이터 답변';
COMMENT ON COLUMN learningdata.memberno is '추가한 회원 번호';


DROP SEQUENCE learningdata_seq;

CREATE SEQUENCE learningdata_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
SELECT * FROM learningdata;

INSERT INTO learningdata(datano, ethical, create_at, ques, ans, memberno)
VALUES(learningdata_seq.nextval,'Y' ,sysdate, '오늘 뭐했어?', '맛있는 밥 먹었어.', 1);

INSERT INTO learningdata (datano, ethical, create_at, ques, ans, memberno)
VALUES (learningdata_seq.NEXTVAL, 'Y', SYSDATE, '응, 초코 케이크였어. 정말 맛있었어!', '초코 케이크라니, 정말 맛있었겠다! 초도 불었어?', 1);

INSERT INTO learningdata (datano, ethical, create_at, ques, ans, memberno)
VALUES (learningdata_seq.NEXTVAL, 'Y', SYSDATE, '응, 친구가 초를 불었어. 우리가 다 같이 생일 축하 노래도 불렀어.', '다 같이 축하 노래를 부르니 친구가 정말 행복했을 것 같아. 선물도 주었어?', 1);

INSERT INTO learningdata (datano, ethical, create_at, ques, ans, memberno)
VALUES (learningdata_seq.NEXTVAL, 'Y', SYSDATE, '응, 내가 준비한 선물을 주었어. 친구가 정말 좋아했어!', '어떤 선물을 준비했어?', 1);

INSERT INTO learningdata (datano, ethical, create_at, ques, ans, memberno)
VALUES (learningdata_seq.NEXTVAL, 'Y', SYSDATE, '친구가 좋아하는 장난감을 사서 줬어.', '정말 좋은 선물이네! 친구가 그 장난감을 받고 기뻐했겠다!', 1);

INSERT INTO learningdata (datano, ethical, create_at, ques, ans, memberno)
VALUES (learningdata_seq.NEXTVAL, 'Y', SYSDATE, '맞아, 바로 열어서 같이 놀았어!', '정말 즐거운 시간 보냈구나! 생일파티에서 또 특별했던 순간이 있었어?', 1);

INSERT INTO learningdata (datano, ethical, create_at, ques, ans, memberno)
VALUES (learningdata_seq.NEXTVAL, 'Y', SYSDATE, '우리 모두 사진을 찍었는데, 사진 속에서 모두가 웃고 있었어. 정말 행복한 순간이었어.', '웃는 얼굴로 사진을 찍으면 좋은 추억이 남지. 생일파티 끝나고도 좋은 기분이 계속 이어졌겠네!', 1);

INSERT INTO learningdata (datano, ethical, create_at, ques, ans, memberno)
VALUES (learningdata_seq.NEXTVAL, 'Y', SYSDATE, '맞아, 정말 좋은 시간이었어.', '다음에 또 친구 생일파티에 갈 기회가 생기면 좋겠다!', 1);

INSERT INTO learningdata (datano, ethical, create_at, ques, ans, memberno)
VALUES (learningdata_seq.NEXTVAL, 'Y', SYSDATE, '응, 정말 기대돼!', '그때도 재미있는 이야기를 들려줄 거지?', 1);

INSERT INTO learningdata (datano, ethical, create_at, ques, ans, memberno)
VALUES (learningdata_seq.NEXTVAL, 'Y', SYSDATE, '당연하지!', '기다릴게~ 그동안 좋은 시간 보내고 또 이야기하자!', 1);

COMMIT;