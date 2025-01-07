DROP TABLE ILLUSTRATION;
CREATE TABLE illustration (
    illustno            NUMBER(10)    NOT NULL    PRIMARY KEY,
    illust              VARCHAR(100)    NOT NULL,
    illust_saved        VARCHAR(100)  NOT NULL,
    illust_size         NUMBER(10)    NULL,
    illust_thumb        VARCHAR(100)  NULL,
    conversationno      NUMBER(10),  
    memberno            NUMBER(10),
    FOREIGN KEY (conversationno) REFERENCES AICONVERSATION(conversationno),
    FOREIGN KEY (memberno) REFERENCES MEMBER(memberno)
);

DROP SEQUENCE ILLUSTRATION_SEQ;
CREATE SEQUENCE illustration_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999 --> NUMBER(7) 대응
  CACHE 2                       -- 2번은 메모리에서만 계산
  NOCYCLE; 

INSERT INTO illustration(illustno, illust, illust_saved, illust_size, illust_thumb, conversationno)
VALUES (0, 'test', 'test', '1', 'test', 0);

INSERT INTO illustration(illustno, illust, illust_saved, illust_size, illust_thumb, conversationno)
VALUES (1, 'test', 'test', '1', 'test', 0);
commit;


SELECT 
    i.illustno,
    i.illust_thumb,
    i.diaryno,
    d.title,
    d.ddate
FROM 
    illustration i
LEFT JOIN 
    diary d
ON 
    i.diaryno = d.diaryno
ORDER BY d.ddate ASC;

SELECT *
FROM user_constraints
WHERE table_name = 'ILLUSTRATION'
AND constraint_type = 'R';
