package dev.mvc.scmenu;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ScmenuVO {
//	CREATE TABLE scmenu (
//			menuno	        NUMBER(10)		    NOT NULL,
//			name	        VARCHAR(20)		    NOT NULL,
//			explan	        VARCHAR(100)		NOT NULL,
//			rdate 			VARCHAR(100)		NOT NULL	
//		);
	
	/**평점 메뉴 번호*/
	private int menuno;
	
	/**평점 메뉴 이름*/
	private String name;
	
	/**평점 메뉴 설명*/
	private String explan;
	
	/**평점 등록일*/
	private String rdate;
}
