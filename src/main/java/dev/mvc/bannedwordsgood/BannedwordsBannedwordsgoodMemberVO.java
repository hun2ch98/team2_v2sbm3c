package dev.mvc.bannedwordsgood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//-- 테이블 3개 join, as 사용시 컬럼명 변경 가능: c.title as n_title
//SELECT g.goodno, g.rdate, g.wordno, b.word as b_title, g.memberno, m.id, m.name
//	FROM bannedwords b, bannedwordsgood g, member m
//	WHERE b.wordno = g.wordno AND g.memberno = m.memberno
//	ORDER BY goodno DESC

@Getter @Setter @ToString
public class BannedwordsBannedwordsgoodMemberVO {
	
	private int goodno;
	
	private String rdate = "";
	
	private int wordno;
	
	private String b_title = "";
	
	private String id = "";
	
	private String name = "";
	
	private int memberno;

}
