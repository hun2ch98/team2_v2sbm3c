DROP TABLE bannedwords;
DROP TABLE bannedwords CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

CREATE TABLE bannedwords (
	wordno	    NUMBER(10)		        NOT NULL,
	word	    VARCHAR(100)		    NOT NULL,
    reason      VARCHAR(500)            NOT NULL,
    goodcnt     NUMBER(7)               NOT NULL,
    memberno    NUMBER(10)              NOT NULL,
	rdate       DATE    NOT NULL
);

COMMENT ON TABLE bannedwords is '금지단어';
COMMENT ON COLUMN bannedwords.wordno is '금지단어 번호';
COMMENT ON COLUMN bannedwords.word is '금지단어';
COMMENT ON COLUMN bannedwords.reason is '금지단어 이유';
COMMENT ON COLUMN bannedwords.goodcnt is '금지단어 좋아요 수';
COMMENT ON COLUMN bannedwords.rdate is '업로드 날짜';

DROP SEQUENCE bannedwords_seq;

CREATE SEQUENCE bannedwords_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지

COMMIT;

SELECT * FROM bannedwords;

INSERT INTO bannedwords(wordno, word, reason, goodcnt, memberno,rdate)
VALUES(bannedwords_seq.nextval, '염병', '“염병”은 특히 장티푸스를 속되게 이르는 말로도 사용됩니다. 장티푸스는 살모넬라균에 의해 발생하는 전염병으로, 고열, 발진, 복통, 설사 등의 증상을 유발합니다. 치료하지 않으면 사망에 이를 수 있는 위험한 질병입니다.', 0, 1, sysdate);

INSERT INTO bannedwords(wordno, word, reason, goodcnt, memberno,rdate)
VALUES(bannedwords_seq.nextval, '지랄', '“지랄”은 원래 뇌전증을 가리키는 순우리말 “지랄병”에서 유래했습니다. 뇌전증 환자의 갑작스럽고 격렬한 증상을 비유적으로 표현하는 데 사용되었죠. 이러한 어원은 “지랄”이라는 단어가 지닌 근본적인 부정적인 이미지를 형성하는 데 영향을 미쳤습니다.', 0, 1, sysdate);

INSERT INTO bannedwords(wordno, word, reason, goodcnt, memberno,rdate)
VALUES(bannedwords_seq.nextval, '시발', '한국어에서 가장 널리 사용되는 비속어 중 하나입니다. 강렬한 분노, 좌절, 불만 등의 감정을 표현하는 데 사용되며, 다양한 상황에서 다양한 의미로 사용될 수 있습니다.', 0, 1, sysdate);

INSERT INTO bannedwords(wordno, word, reason, goodcnt, memberno, rdate)
VALUES(bannedwords_seq.nextval, '병신', '“병신”은 원래 병에 걸린 사람을 의미하는 순우리말입니다. 하지만 시간이 흐르면서 육체적으로 불편하거나 정신적으로 문제가 있는 사람을 비하하는 의미로 사용되었습니다. 특히, 장애인을 향한 차별과 혐오를 표현하는 단어로 여겨지고 있습니다.', 0, 1, sysdate);

-- 추천
UPDATE bannedwords
SET goodcnt = goodcnt +1
WHERE wordno = 1;

-- 비추천
UPDATE bannedwords
SET goodcnt = goodcnt -1
WHERE wordno = 1;

SELECT b.wordno, b.word, b.reason, b.goodcnt, b.memberno, b.rdate,
       m.id AS id, m.name AS name,
       ROW_NUMBER() OVER (ORDER BY b.wordno DESC) AS rnum
FROM bannedwords b
LEFT JOIN member m ON b.memberno = m.memberno;
WHERE (#{word} IS NULL OR #{word} = '' OR b.word = #{word});

