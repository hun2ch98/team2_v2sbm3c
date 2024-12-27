DROP TABLE emotion;
DROP TABLE emotion CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE emotion (
	emono	NUMBER(10)		    NOT NULL,
	type	VARCHAR(50)		    NOT NULL,
    explan  VARCHAR(500)        NOT NULL,
	img	    VARCHAR(500)		NULL
);

COMMENT ON TABLE emotion is '감정';
COMMENT ON COLUMN emotion.emono is '감정 번호';
COMMENT ON COLUMN emotion.type is '감정 유형';
COMMENT ON COLUMN emotion.explan is '감정 내용';
COMMENT ON COLUMN emotion.img is '감정 이미지';

DROP SEQUENCE emotion_seq;

CREATE SEQUENCE emotion_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '무심하다', '감정이 드러나지 않고 담담한 상태.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '지루하다', '반복적이거나 흥미가 없어 무료한 상태.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '아쉽다', '무언가가 조금 부족해 아쉬운 상태.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '다짐하다', '스스로 결심하거나 어떤 일을 이루기 위해 의지를 다잡는 상태.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '다행스럽다', '나쁜 상황이 아닌 결과로 인해 안도하는 상태.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '슬프다', '우울하거나 상심한 상태를 표현합니다.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '사랑스럽다', '상대를 향한 애정을 표현합니다.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '장난스럽다', '애교와 장난기가 가득한 상태.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '화나다', '짜증이 나거나 기분이 나쁜 상태.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '행복하다', '기쁘고 만족스러운 상태.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '설레다', '두근거리며 기대에 차 있는 상태입니다.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '놀라다', '무언가 충격적인 것을 보고 놀란 상태.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '재미있다', '흥미를 느끼고 즐거운 상태.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '고맙다', '누군가의 도움이나 배려에 대해 감사하고 이에 보답하고 싶은 따뜻한 마음을 느끼는 상태.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '기쁘다', '밝고 활기찬 상태.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '답답하다', '무언가가 잘 풀리지 않아 답답한 상태.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '맛있었다', '맛있는 걸 먹어 행복한 상태.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '그립다', '과거의 누군가나 무언가를 보고 싶거나 다시 만나고 싶은 감정을 느끼는 상태.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '축하하다', '상대방의 기쁜일이나 성과를 진심으로 기뻐하며 축복하는 상태.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '걱정하다', '생각이 많고 불안한 상태입니다.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '피곤하다', '졸리거나 피곤한 상태입니다.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '놀라다', '예상치 못한 상황에 반응하는 감정입니다.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '행복한 상상', '좋은 꿈이나 생각에 빠져 있는 상태입니다.');

INSERT INTO emotion(emono, type, explan)
VALUES(emotion_seq.nextval, '슬프다', '상심하거나 아픈 상태를 나타냅니다.');


SELECT * FROM emotion;

COMMIT;

