package dev.mvc.score;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ScoreVO {
	
//	CREATE TABLE score (
//		    scoreno    NUMBER(10)     NOT NULL,
//		    jumsu      NUMBER(2,1)    NULL,
//		    rdate      DATE           NOT NULL,
//		    memberno   NUMBER(10)     NOT NULL,
//		    CONSTRAINT chk_jumsu CHECK (MOD(jumsu, 0.5) = 0 AND jumsu <= 5)
//		);

	/** 평점 번호 */
    private int scoreno;

    /** 평점 (0.5단위, 최대 5점) */
    private String jumsu;

    /** 등록일 */
    private String rdate;  // 날짜 형식을 LocalDate로 변경

    /** 회원 번호 */
    private int memberno;


}
