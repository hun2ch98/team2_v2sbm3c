package dev.mvc.grade;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class GradeVO {
  
//  CREATE TABLE grade (
//      gradeno         NUMBER(5)       NOT NULL    PRIMARY KEY,   -- 등급 번호 
//      grade_name      VARCHAR2(20)    NOT NULL,                  -- 등급 이름
//      gdescription    VARCHAR2(255)   NULL,                      -- 등급 설명
//      rdate           DATE            NOT NULL,                  -- 생성 날짜
//      file1           VARCHAR(200)    NULL,                      -- 원본 파일 이름
//      file1saved      VARCHAR2(100)   NULL,                      -- 서버에 저장된 파일 이름
//      thumb1          VARCHAR2(100)   NULL,                      -- 썸네일 이미지 파일 이름
//      size1           NUMBER(10)      NULL                       -- 파일 크기
//  );
  
    /** 등급 번호, Sequence를 통해 자동 생성 */
    private int gradeno;
  
    /** 등급 이름 */
    @NotEmpty(message="등급 이름은 필수 사항입니다.")
    @Size(min=1, max=20)
    private String grade_name = "";
  
    /** 등급 내용 설명*/
    private String gdescription;
  
    /** 생성 날짜 */
    private Date rdate;
  
  //파일 업로드 관련
    // -----------------------------------------------------------------------------------
    /**
    이미지 파일
    <input type='file' class="form-control" name='file1MF' id='file1MF' 
               value='' placeholder="파일 선택">
    */
    private MultipartFile file1MF = null;
    /** 메인 이미지 크기 단위, 파일 크기 */
    private String size1_label = "";
    /** 메인 이미지 */
    private String file1 = "";
    /** 실제 저장된 메인 이미지 */
    private String file1saved = "";
    /** 메인 이미지 preview */
    private String thumb1 = "";
    /** 메인 이미지 크기 */
    private long size1 = 0;
}
