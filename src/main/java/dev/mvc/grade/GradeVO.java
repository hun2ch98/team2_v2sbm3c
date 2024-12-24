package dev.mvc.grade;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeVO {
  private int gradeno;                         // 등급 번호
  
  private String grade_name;                   // 등급 이름
  
  private String grade_img;                    // 등급별 이미지 경로
  
  private String img_saved;                    // 실제 저장된 이미지 경로
  
  private int img_size;                        // 이미지 크기
  
  private String gdescription;                 // 등급 설명
  
  private int min_points;                      // 최소 포인트
  
  private int max_points;                      // 최대 포인트
  
  private String created_at;                   // 생성 날짜
  
  private String update_at;                    // 수정 날짜
}
