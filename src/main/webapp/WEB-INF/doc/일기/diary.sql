CREATE TABLE DIARY (
    diaryno NUMBER(30) PRIMARY KEY,
    title VARCHAR2(50) ,
    date DATE,
    summary CLOB,
    weather_code NUMBER(10),
    emono NUMBER(20),
    memberno NUMBER(30)

);
