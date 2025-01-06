package dev.mvc.bannedwords;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class BannedwordsVO {
	
//	CREATE TABLE bannedwords (
//			wordno	    NUMBER(10)		        NOT NULL,
//			word	    VARCHAR(100)		    NOT NULL,
//		    reason      VARCHAR(100)            NOT NULL,
//		    goodcnt     NUMBER(7)               NULL,
//			rdate       DATE    NOT NULL
//		);
	
	/**금지단어 번호 */
	private int wordno; // 시퀀스로 자동 채워짐, 따로 검증할 필요 없음
	
	/**금지단어*/
   @NotEmpty(message="금지어 입력은 필수 사항입니다.")
	private String word = "";
	
	/**금지단어 이유*/
   @NotEmpty(message="설명 입력은 필수 사항입니다.")
	private String reason = "";
	
	/**금지단어 등록일*/
	private String rdate;
	
	/**좋아요 수*/
	private int goodcnt;
	
	/**등록한 사람*/
	private int memberno;
}
