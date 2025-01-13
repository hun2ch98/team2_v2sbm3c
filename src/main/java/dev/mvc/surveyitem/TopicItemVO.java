package dev.mvc.surveyitem;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class TopicItemVO {
  
  /**
   * -- 테이블 2개 join
    SELECT i.itemno, i.surveyno, i.item_seq, i.item, i.item_cnt, s.topic
    FROM survey s, surveyitem i
    WHERE s.surveyno = i.surveyno
    ORDER BY itemno DESC;
   */
  
  /** 항목 번호 */
  private int itemno;
  
  /** 설문조사 번호 */
  private int surveyno;
  
  /** 항목 출력 순서 */
  private int item_seq;
  
  /** 항목 */
  private String item="";
  
  /** 항목 선택 인원 */
  private int item_cnt=0;
  
  /** 설문조사 주제 */
  private String topic = "";

}
