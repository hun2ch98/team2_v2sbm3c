CREATE TABLE Diary (
    diaryno NUMBER(100) PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    date DATE,
    summary VARCHAR(1000),
    weather_code NUMBER(10),
    emono NUMBER(20),
    conversationno NUMBER(100), -- NULL을 허용
    memberno NUMBER(100) NOT NULL,
    FOREIGN KEY (weather_code) REFERENCES Weather(weather_code),
    FOREIGN KEY (emono) REFERENCES Emotion(emono),
    FOREIGN KEY (conversationno) REFERENCES AIConversation(conversationno) ON DELETE SET NULL,
    FOREIGN KEY (memberno) REFERENCES Member(memberno)
);
