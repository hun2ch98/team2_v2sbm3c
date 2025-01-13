package dev.mvc.modeltraining;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ModeltrainingVO {
	
//	CREATE TABLE modeltraining (
//			trainingno	    NUMBER(10)	    NOT NULL,
//			name	        VARCHAR(100)	NOT NULL,
//			status	        VARCHAR(100)	NOT NULL,
//			accuracy	    NUMBER(5, 2)		NOT NULL,
//			rdate	        	DATE	        		NOT NULL,
//			notes	        	VARCHAR(100)	NOT NULL,
//			st_time	        DATE	            	NOT NULL,
//			end_time	    DATE	       			NULL,
//			memberno	    NUMBER(10)	    NOT NULL
//		);
	
	/**모델 번호 */
	private int trainingno;
	
	/**모델 이름 */
	@NotEmpty(message="모델 이름 입력은 필수 사항입니다.")
	private String name = "";
	
	/**학습 상태 */
	@NotEmpty(message="학습 상태 입력은 필수 사항입니다.")
	private String status = "";
	
	/**학습 정확도 */
	@NotEmpty(message="학습 정확도 입력은 필수 사항입니다.")
	private int accuracy;
	
	/**학습 날짜 */
	private String rdate = "";
	
	/**학습 관련 메모 */
	@NotEmpty(message="학습 관련 메모 입력은 필수 사항입니다.")
	private String notes = "";
	
	/**시작 시간 */
	@NotEmpty(message="시작 날짜 입력은 필수 사항입니다.")
	private String st_time = "";
	
	/**종료 시간 */
	private String end_time = "";
	
	/**등록한 회원 번호 */
	private int memberno;
	
	
}
