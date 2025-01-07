package dev.mvc.bannedwordsgood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class BannedwordsgoodVO {
	
//	CREATE TABLE bannedwordsgood (
//			goodno	    NUMBER(10)		NOT NULL,
//			rdate	    DATE		    NOT NULL,
//			wordno	    NUMBER(10)		NOT NULL,
//			memberno	NUMBER(10)		NOT NULL
//		);

	/**금지단어 좋아요 번호*/
	private int goodno;
	
	/**등록일*/
	private String rdate;
	
	/**금지단어 번호*/
	private int wordno;
	
	/**회원 번호*/
	private int memberno;
}
