package dev.mvc.score;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ScoreVO {
	
//	CREATE TABLE score (
//		    scoreno                 NUMBER(10)  PRIMARY KEY NOT NULL,
//		    diary_score             NUMBER(1)   NOT NULL,
//		    calendar_score          NUMBER(1)   NOT NULL,
//		    chat_score              NUMBER(1)   NOT NULL,
//		    login_score             NUMBER(1)   NOT NULL,
//		    survey_score            NUMBER(1)   NOT NULL,
//		    drawing_score           NUMBER(1)   NOT NULL,
//		    emotion_score           NUMBER(1)   NOT NULL,
//		    weather_score           NUMBER(1)   NOT NULL,
//		    notice_score            NUMBER(1)   NOT NULL,
//		    word_score              NUMBER(1)   NOT NULL,
//		    total                   NUMBER(1)   NOT NULL,
//		    memberno                NUMBER(10)  NOT NULL,
//		    rdate                   VARCHAR(10) NOT NULL
//		);

	/** 평점 번호 */
	private int scoreno;

	/** 일기 점수 */
	private int diary_score;

	/** 달력 점수 */
	private int calendar_score;

	/** 채팅 점수 */
	private int chat_score;

	/** 로그인 점수 */
	private int login_score;

	/** 설문조사 점수 */
	private int survey_score;

	/** 그림 점수 */
	private int drawing_score;

	/** 감정 점수 */
	private int emotion_score;

	/** 날씨 점수 */
	private int weather_score;

	/** 공지사항 점수 */
	private int notice_score;

	/** 금지단어 점수 */
	private int word_score;

	/** 총점수 */
	private int total;

	/** 회원 번호 */
	private int memberno;

	/** 등록일 */
	private String rdate;

}
