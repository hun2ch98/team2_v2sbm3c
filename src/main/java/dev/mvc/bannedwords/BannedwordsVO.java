package dev.mvc.bannedwords;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class BannedwordsVO {
	
	/**
	 * CREATE TABLE bannedwords (
	wordno	    NUMBER(10)		    NOT NULL,
	word	    VARCHAR(50)		NOT NULL,
	reason	    VARCHAR(50)		NOT NULL,
	rdate	    DATE		        NOT NULL
);
	 */
	
	/**금지단어 번호 */
	private int wordno;
	
	/**금지단어*/
	private String word="";
	
	/**금지단어 이유*/
	private String reason="";
	
	/**금지단어 등록일*/
	private String rdate;
	
	/**등록한 사람*/
	private int memberno;

}
